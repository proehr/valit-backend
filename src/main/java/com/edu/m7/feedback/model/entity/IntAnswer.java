package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "int_answer")
public class IntAnswer extends Answer{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_answer_id", nullable = false)
    private Long intAnswerId;

    @Column(name = "value")
    @Setter
    private Integer value;
}