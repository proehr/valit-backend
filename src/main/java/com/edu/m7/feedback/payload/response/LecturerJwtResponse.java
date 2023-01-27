package com.edu.m7.feedback.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LecturerJwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;

    private Long lecturerId;
    private String title;
    private String firstName;
    private String lastName;

    public LecturerJwtResponse(
            String token,
            String username,
            Long lecturerId,
            String title,
            String firstName,
            String lastName
    ) {
        this.token = token;
        this.username = username;
        this.lecturerId = lecturerId;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
