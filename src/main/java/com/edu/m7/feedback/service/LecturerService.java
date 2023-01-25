package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.AccountRepository;
import com.edu.m7.feedback.model.repository.LecturerRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import com.edu.m7.feedback.payload.request.RegistrationRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LecturerService {

    private final LecturerRepository repository;
    private final AccountRepository accountRepository;

    public LecturerService(LecturerRepository lecturerRepository, AccountRepository accountRepository) {
        this.repository = lecturerRepository;
        this.accountRepository = accountRepository;
    }

    public Lecturer getLecturerByUsername(String username) {
        Account account = accountRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No User found for username -> " + username));

        Optional<Lecturer> optionalEntity = repository.findByAccount(account);
        return optionalEntity.orElseThrow();
    }

    public Lecturer createLecturer(String username, String title, String firstName, String lastName) {
        Account account = accountRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No User found for username -> " + username));

        Lecturer lecturer = new Lecturer();
        lecturer.setTitle(title);
        lecturer.setFirstName(firstName);
        lecturer.setLastName(lastName);
        lecturer.setAccount(account);
        repository.save(lecturer);
        return lecturer;
    }

    /**
     * Method used for authorization checks in multiple controllers
     * @param principal the logged-in user
     * @return the lecturer entity affiliated with the given principal user
     */
    public Lecturer getLecturer(Principal principal) {
        //get username of logged in account
        String username = principal.getName();

        //look for the account
        Optional<Account> optionalEntity = accountRepository.findByUsername(username);

        // return the logged in account Entity
        Account account = optionalEntity.orElseThrow();

        // look for the lecturer associated with the account
        Optional<Lecturer> optionalLecturer = repository.findByAccount(account);

        // return the associated lecturer entity
        return optionalLecturer.orElseThrow();
    }
}
