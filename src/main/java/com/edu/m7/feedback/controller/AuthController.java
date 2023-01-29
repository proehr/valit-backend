package com.edu.m7.feedback.controller;

import javax.validation.Valid;

import com.edu.m7.feedback.model.AccountType;
import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.payload.request.LoginRequest;
import com.edu.m7.feedback.payload.request.RegistrationRequest;
import com.edu.m7.feedback.payload.request.SignupRequest;
import com.edu.m7.feedback.payload.response.JwtResponse;
import com.edu.m7.feedback.payload.response.LecturerJwtResponse;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.security.FeedbackUserDetails;
import com.edu.m7.feedback.security.jwt.JwtUtils;
import com.edu.m7.feedback.service.AccountService;
import com.edu.m7.feedback.service.LecturerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final AccountService accountService;

    private final LecturerService lecturerService;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public AuthController(
            AuthenticationManager authenticationManager,
            AccountService accountService,
            LecturerService lecturerService,
            PasswordEncoder encoder,
            JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.lecturerService = lecturerService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin-as-lecturer")
    public ResponseEntity<LecturerJwtResponse> authenticateLecturer(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        FeedbackUserDetails userDetails = (FeedbackUserDetails) authentication.getPrincipal();
        Lecturer lecturer = lecturerService.getLecturerByUsername(userDetails.getUsername());
        return ResponseEntity.ok(new LecturerJwtResponse(jwt,
                userDetails.getUsername(),
                lecturer.getLecturerId(),
                lecturer.getTitle(),
                lecturer.getFirstName(),
                lecturer.getLastName())
        );
    }

    @PostMapping("/signin-as-student")
    public ResponseEntity<JwtResponse> authenticateStudent(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @GetMapping("/userExists")
    public ResponseEntity<Boolean> userExists(@RequestBody String username) {
        return ResponseEntity.ok(accountService.userExists(username));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (accountService.userExists(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("errorEmailTaken"));
        }

        // Create new user's account
        Account account = new Account(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getAccountType());
        accountService.createUser(account);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/full-register")
    public ResponseEntity<MessageResponse> registerUserWithProfile(
            @Valid @RequestBody RegistrationRequest registrationRequest
    ) {
        SignupRequest signupRequest = new SignupRequest(
                registrationRequest.getUsername(),
                AccountType.LECTURER,
                registrationRequest.getPassword()
        );
        ResponseEntity<MessageResponse> signupResponse = registerUser(signupRequest);

        if (signupResponse.getStatusCodeValue() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("errorEmailTaken"));
        }

        lecturerService.createLecturer(
                registrationRequest.getUsername(),
                registrationRequest.getTitle(),
                registrationRequest.getFirstName(),
                registrationRequest.getLastName()
        );
        return ResponseEntity.ok(new MessageResponse("User registered and created lecturer successfully!"));
    }
}
