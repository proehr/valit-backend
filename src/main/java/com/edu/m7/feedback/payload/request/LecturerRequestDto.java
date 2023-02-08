package com.edu.m7.feedback.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LecturerRequestDto {
    private String title;
    private String firstName;
    private String lastName;
}
