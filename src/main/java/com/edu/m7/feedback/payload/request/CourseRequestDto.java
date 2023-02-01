package com.edu.m7.feedback.payload.request;

import com.edu.m7.feedback.model.IntervalType;
import com.edu.m7.feedback.model.entity.Semester;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class CourseRequestDto {
    private String name;
    private String degree;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private Integer studentCount;
    private IntervalType interval;
    private DayOfWeek weekday;
    private Long semester;
    private LocalDate finalEvaluationDate;

}
