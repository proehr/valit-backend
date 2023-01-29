package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "question_choice")
@NoArgsConstructor
public class QuestionChoice {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "question_choice_id")
    private long questionChoiceId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private ChoiceQuestion question;

    @Setter
    @Column(name = "choice_key")
    private String choiceKey;

    @Setter
    @Column(name = "choice_value")
    private Integer choiceValue;

}
