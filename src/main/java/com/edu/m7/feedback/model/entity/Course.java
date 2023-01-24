package com.edu.m7.feedback.model.entity;

import com.edu.m7.feedback.model.dto.CourseDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "degree")
    private String degree;

    @Column(name = "time_start")
    private LocalTime timeStart;

    @Column(name = "time_end")
    private LocalTime timeEnd;

    @Column(name = "dates", columnDefinition = "date[](13)")
    private String dates;

    @Column(name = "student_count")
    private Integer studentCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecturer_fk")
    private Lecturer lecturer;
    @OneToMany(mappedBy = "course")
    private Set<Evaluation> evaluations = new LinkedHashSet<>();

    public static Course toEntity(CourseDto courseDto){
        return Course.builder()
                .id(courseDto.getId())
                .name(courseDto.getName())
                .degree(courseDto.getDegree())
                .timeStart(courseDto.getTimeStart())
                .timeEnd(courseDto.getTimeEnd())
                .dates(courseDto.getDates())
                .studentCount(courseDto.getStudentCount())
                .lecturer(courseDto.getLecturer())
                .evaluations(courseDto.getEvaluations())
                .build();
    }


}