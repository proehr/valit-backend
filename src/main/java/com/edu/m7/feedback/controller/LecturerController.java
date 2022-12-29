package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.AccountRepository;
import com.edu.m7.feedback.model.repository.LecturerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LecturerController {

    private final LecturerRepository repository;
    private final AccountRepository accountRepository;

    public LecturerController(LecturerRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/profile")
    Lecturer getLecturer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Account> optionalEntity = accountRepository.findByUsername(username);
        Account account  = optionalEntity.orElseThrow();
        return repository.findByAccount(account).orElseThrow();
    }
}
