package com.edu.m7.feedback.payload.request;

import com.edu.m7.feedback.model.AccountType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    private AccountType accountType;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
