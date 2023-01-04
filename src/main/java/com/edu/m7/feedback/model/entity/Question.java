package com.edu.m7.feedback.model.entity;

import com.edu.m7.feedback.model.QuestionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation")
    private Evaluation evaluation;
    @Column(name = "value")
    private String value;

    @Column(name = "type", columnDefinition = "question_type")
    @Enumerated(EnumType.STRING)
    private QuestionType type;

}