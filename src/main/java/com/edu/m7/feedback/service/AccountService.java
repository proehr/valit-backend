package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.mapping.FeedbackUserDetailsMapper;
import com.edu.m7.feedback.model.repository.AccountRepository;
import com.edu.m7.feedback.security.FeedbackUserDetails;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
public class AccountService implements UserDetailsManager {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Mappers.getMapper(FeedbackUserDetailsMapper.class).accountToUserDetails(
                repository
                        .findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("No user found with username = " + username))
        );
    }

    public Long getAccountIdByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username = " + username))
                .getAccountId();
    }

    @Override
    public void createUser(UserDetails user) {
        createUser(
                Mappers.getMapper(FeedbackUserDetailsMapper.class).userDetailsToAccount((FeedbackUserDetails) user)
        );
    }

    public void createUser(@Valid Account account) {
        repository.save(account);
    }

    @Override
    public void updateUser(UserDetails user) {
        updateUser(
                Mappers.getMapper(FeedbackUserDetailsMapper.class).userDetailsToAccount((FeedbackUserDetails) user)
        );
    }

    public void updateUser(@Valid Account account) {
        repository.save(account);
    }

    @Override
    public void deleteUser(String username) {
        Account account = repository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No User found for username -> " + username));
        repository.delete(account);
    }

    /**
     * This method assumes that both oldPassword and the newPassword params
     * are encoded with configured passwordEncoder
     *
     * @param oldPassword the old password of the user
     * @param newPassword the new password of the user
     */
    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        Account userDetails = repository
                .findByPassword(oldPassword)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid password "));
        userDetails.setPassword(newPassword);
        repository.save(userDetails);
    }

    @Override
    public boolean userExists(String username) {
        return repository.findByUsername(username).isPresent();
    }
}
