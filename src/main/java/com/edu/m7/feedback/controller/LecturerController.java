package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.AccountRepository;
import com.edu.m7.feedback.model.repository.LecturerRepository;
import com.edu.m7.feedback.payload.response.MessageResponse;
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
    ResponseEntity<Lecturer> getLecturer(Principal principal) {
        String username = principal.getName();
        Optional<Account> optionalEntity = accountRepository.findByUsername(username);
        Account account  = optionalEntity.orElseThrow();
        return ResponseEntity.ok(repository.findByAccount(account).orElseThrow());
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PostMapping("/lecturer")
    // TODO: remove entity from controller method
    ResponseEntity<MessageResponse> createOrUpdateLecturer(@RequestBody Lecturer newLecturer, Principal principal) {
        Account account = getSignedInAccount(principal);
        newLecturer.setAccount(account);
         Optional<Lecturer> optionalLecturer = repository.findByAccount(account);
        if (optionalLecturer.isPresent()) {
            Lecturer lecturer = optionalLecturer.get();
            newLecturer.setLecturerId(lecturer.getLecturerId());
            newLecturer.setAccount(lecturer.getAccount());
            repository.save(newLecturer);
            return ResponseEntity.ok(new MessageResponse("Profile updated successfully!"));
        } else {
            repository.save(newLecturer);
            return ResponseEntity.ok(new MessageResponse("Profile created successfully!"));
        }
    }
}
