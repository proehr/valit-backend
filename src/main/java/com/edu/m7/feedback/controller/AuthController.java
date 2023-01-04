package com.edu.m7.feedback.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.payload.request.LoginRequest;
import com.edu.m7.feedback.payload.request.SignupRequest;
import com.edu.m7.feedback.payload.response.JwtResponse;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.security.FeedbackUserDetails;
import com.edu.m7.feedback.security.jwt.JwtUtils;
import com.edu.m7.feedback.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public AuthController(
            AuthenticationManager authenticationManager,
            AccountService accountService,
            PasswordEncoder encoder,
            JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        FeedbackUserDetails userDetails = (FeedbackUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (accountService.userExists(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account
        Account account = new Account(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getAccountType());
        accountService.createUser(account);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/userExists")
    public ResponseEntity<Boolean> userExists(@RequestBody String username) {
        return ResponseEntity.ok(accountService.userExists(username));
    }
}
