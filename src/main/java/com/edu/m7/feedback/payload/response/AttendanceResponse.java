package com.edu.m7.feedback.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class AttendanceResponse {

    private Integer attendance;
    private LocalDate date;

}
