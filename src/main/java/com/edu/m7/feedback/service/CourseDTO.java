package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long id;
    private String name;
    private String degree;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String dates;
    private Integer studentCount;
    private Long lecturerId;
    private String lecturerName;
    private String lecturerEmail;

}
