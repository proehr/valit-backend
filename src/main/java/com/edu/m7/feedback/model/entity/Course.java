package com.edu.m7.feedback.model.entity;

import com.edu.m7.feedback.model.IntervalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "course")
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Long id;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "degree")
    @Setter
    private String degree;

    @Column(name = "time_start")
    @Setter
    private LocalTime timeStart;

    @Column(name = "time_end")
    @Setter
    private LocalTime timeEnd;

    @OneToMany(mappedBy = "course")
    @Setter
    private Set<Date> dates = new LinkedHashSet<>();

    @Column(name = "student_count")
    @Setter
    private Integer studentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_fk")
    @Setter
    private Lecturer lecturer;

    @OneToMany(mappedBy = "course")
    private Set<Evaluation> evaluations = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "interval")
    @Setter
    private IntervalType interval;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_fk")
    private Semester semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "weekday")
    @Setter
    private DayOfWeek weekday;

}
