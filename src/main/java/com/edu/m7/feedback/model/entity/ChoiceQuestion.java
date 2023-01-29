package com.edu.m7.feedback.model.entity;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity(name = "ChoiceQuestion")
@DiscriminatorValue("SCALE")
public class ChoiceQuestion extends Question {

    @OneToMany(mappedBy = "question")
    private Set<QuestionChoice> choices = new LinkedHashSet<>();

}
