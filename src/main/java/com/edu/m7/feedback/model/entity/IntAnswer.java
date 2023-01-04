package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "int_answer")
public class IntAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_answer_id", nullable = false)
    private Long id;

    @Column(name = "value")
    @Setter
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question")
    @Setter
    private Question question;

}