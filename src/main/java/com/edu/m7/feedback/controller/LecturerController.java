package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.AccountRepository;
import com.edu.m7.feedback.model.repository.LecturerRepository;
import com.edu.m7.feedback.payload.request.LecturerRequestDto;
import com.edu.m7.feedback.payload.response.LecturerJwtResponse;
import com.edu.m7.feedback.payload.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LecturerController {
    private final LecturerRepository repository;

    private final AccountRepository accountRepository;

    public LecturerController(LecturerRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    Account getSignedInAccount(Principal principal) {
        String username = principal.getName();
        Optional<Account> optionalEntity = accountRepository.findByUsername(username);
        return optionalEntity.orElseThrow();
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping(value = "/profile")
    ResponseEntity<LecturerJwtResponse> getLecturer(Principal principal) {
        String username = principal.getName();
        Account account = getSignedInAccount(principal);
        Optional<Lecturer> optionalLecturer = repository.findByAccount(account);
        if (optionalLecturer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Lecturer lecturer = optionalLecturer.get();
        return ResponseEntity.ok(new LecturerJwtResponse("",
                username,
                lecturer.getLecturerId(),
                lecturer.getTitle(),
                lecturer.getFirstName(),
                lecturer.getLastName())
        );
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PostMapping("/lecturer")
    ResponseEntity<MessageResponse> updateLecturer(@RequestBody LecturerRequestDto newLecturer, Principal principal) {
        Account account = getSignedInAccount(principal);
        Lecturer lecturer = repository.findByAccount(account).orElseThrow();
        lecturer.setTitle(newLecturer.getTitle());
        lecturer.setFirstName(newLecturer.getFirstName());
        lecturer.setLastName(newLecturer.getLastName());
        repository.save(lecturer);
        return ResponseEntity.ok(new MessageResponse("Profile updated successfully!"));
    }

}
