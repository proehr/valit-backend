package com.edu.m7.feedback.payload.response;

import com.edu.m7.feedback.model.entity.Lecturer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;

    private Long lecturerId;
    private String title;
    private String firstName;
    private String lastName;

    public JwtResponse(String token, String username, Long lecturerId, String title, String firstName, String lastName) {
        this.token = token;
        this.username = username;
        this.lecturerId = lecturerId;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
