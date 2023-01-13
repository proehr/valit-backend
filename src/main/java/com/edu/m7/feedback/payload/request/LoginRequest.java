package com.edu.m7.feedback.payload.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
