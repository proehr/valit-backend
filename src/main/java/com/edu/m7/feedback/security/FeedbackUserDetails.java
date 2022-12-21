package com.edu.m7.feedback.security;

import com.edu.m7.feedback.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class FeedbackUserDetails implements UserDetails {

    private static final long serialVersionUID = 2;

    private final boolean accountNonExpired = true;
    private final boolean accountNonLocked = true;
    private final boolean credentialsNonExpired = true;
    private final boolean enabled = true;

    private String password;
    private String username;
    private AccountType type;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return type.getGrantedAuthorities();
    }
}
