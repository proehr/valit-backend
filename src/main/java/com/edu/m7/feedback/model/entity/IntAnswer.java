package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "int_answer")
@NoArgsConstructor
public class IntAnswer extends Answer{
    @Column(name = "value")
    @Setter
    private Integer value;

}
