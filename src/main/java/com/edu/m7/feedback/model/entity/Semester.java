package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private long semesterId;

    @Column(name = "semester_name")
    private String semesterName;
   // @Setter
    @Column(name = "start_date")
    private LocalDate startDate;

    //@Setter
    @Column(name = "end_date")
    private LocalDate endDate;
}
