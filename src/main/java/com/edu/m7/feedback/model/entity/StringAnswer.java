package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "string_answer")
public class StringAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "string_answer_id", nullable = false)
    private Long id;

    @Column(name = "value")
    @Setter
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question")
    @Setter
    private Question question;

}