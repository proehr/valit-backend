package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "string_answer")
@NoArgsConstructor
public class StringAnswer extends Answer{
    @Column(name = "value")
    @Setter
    private String value;

}
