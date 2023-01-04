package com.edu.m7.feedback.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum AccountType {

    LECTURER("LECTURER"),
    ADMIN("ADMIN"),
    STUDENT("STUDENT");

    @Getter
    private final Set<GrantedAuthority> grantedAuthorities;

    AccountType(String... authorities) {
        this.grantedAuthorities = Arrays.stream(authorities)
                .map(FeedbackGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
