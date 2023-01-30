package com.edu.m7.feedback.payload.response;

import com.edu.m7.feedback.model.IntervalType;
import com.edu.m7.feedback.model.dto.SemesterDto;
import com.edu.m7.feedback.model.entity.Evaluation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CourseResponseDto {

    private Long id;
    private String name;
    private String degree;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private Set<LocalDate> dates;
    private Integer studentCount;
    //private Set<EvaluationResponseDto> evaluations;
    private IntervalType interval;
    private SemesterDto semester;
    private DayOfWeek weekday;

}
