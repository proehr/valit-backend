package com.edu.m7.feedback.model;

import org.springframework.security.core.GrantedAuthority;

public class FeedbackGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = 1;
    private final String authority;

    public FeedbackGrantedAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
