package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.AccountRepository;
import com.edu.m7.feedback.model.repository.LecturerRepository;
import com.edu.m7.feedback.payload.request.RegistrationRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LecturerService {

    private final LecturerRepository repository;
    private final AccountRepository accountRepository;

    public LecturerService(LecturerRepository lecturerRepository, AccountRepository accountRepository) {
        this.repository = lecturerRepository;
        this.accountRepository = accountRepository;
    }

    public Lecturer createLecturer(String username, RegistrationRequest registrationRequest) {
        Account account = accountRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No User found for username -> " + username));

        Lecturer lecturer = new Lecturer();
        lecturer.setTitle(registrationRequest.getTitle());
        lecturer.setFirstName(registrationRequest.getFirstName());
        lecturer.setLastName(registrationRequest.getLastName());
        lecturer.setAccount(account);
        repository.save(lecturer);
        return lecturer;
    }
}
