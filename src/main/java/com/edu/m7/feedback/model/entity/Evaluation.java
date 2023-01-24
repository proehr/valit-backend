package com.edu.m7.feedback.model.entity;

import com.edu.m7.feedback.model.EvaluationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "evaluation")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_fk")
    private Course course;

    @Column(name = "type", columnDefinition = "evaluation_type")
    @Enumerated(EnumType.STRING)
    private EvaluationType type;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_fk")
    private Date date;

}