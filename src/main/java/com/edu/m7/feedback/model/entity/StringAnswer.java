package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "string_answer")
public class StringAnswer extends Answer{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "string_answer_id", nullable = false)
    private Long stringAnswerId;

    @Column(name = "value")
    @Setter
    private String value;
}