package com.edu.m7.feedback.model;

import org.springframework.security.core.GrantedAuthority;

public class FeedbackGrantedAuthority implements GrantedAuthority {
    String ROLE_PREFIX = "ROLE_";

    private static final long serialVersionUID = 1;
    private final String authority;

    public FeedbackGrantedAuthority(String authority) {
        this.authority = ROLE_PREFIX + authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
