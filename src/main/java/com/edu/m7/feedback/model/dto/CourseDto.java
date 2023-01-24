package com.edu.m7.feedback.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class CourseDto {

    private Long id;
    private String name;
    private String degree;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String dates;
    private Integer studentCount;

}
