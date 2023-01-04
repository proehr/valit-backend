package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.AccountRepository;
import com.edu.m7.feedback.model.repository.LecturerRepository;
import com.edu.m7.feedback.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
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
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @ResponseBody
    Lecturer getLecturer(Principal principal) {
        String username = principal.getName();
        Optional<Account> optionalEntity = accountRepository.findByUsername(username);
        Account account  = optionalEntity.orElseThrow();
        return repository.findByAccount(account).orElseThrow();
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PostMapping("/lecturer")
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
