package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

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
    private LocalDate[] dates;

    @Column(name = "student_count")
    private Integer studentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_fk")
    private Lecturer lecturer;
    @OneToMany(mappedBy = "course")
    private Set<Evaluation> evaluations = new LinkedHashSet<>();

}