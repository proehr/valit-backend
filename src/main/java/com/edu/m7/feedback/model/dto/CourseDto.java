package com.edu.m7.feedback.model.dto;

import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.entity.Lecturer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private Long id;

    private String name;

    private String degree;

    private LocalTime timeStart;

    private LocalTime timeEnd;

    private String dates;

    private Integer studentCount;

    private Lecturer lecturer;
    private Set<Evaluation> evaluations = new LinkedHashSet<>();

    public static CourseDto toDto(Course entity) {
        return CourseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .degree(entity.getDegree())
                .timeStart(entity.getTimeStart())
                .timeEnd(entity.getTimeEnd())
                .dates(entity.getDates())
                .studentCount(entity.getStudentCount())
                .lecturer(entity.getLecturer())
                .evaluations(entity.getEvaluations())
                .build();
    }

    public static List<CourseDto> toDto(List<Course> entityList) {
        return (List<CourseDto>) entityList.stream().map(
                course -> toDto(course)
        ).collect(Collectors.toList());
    }

}
