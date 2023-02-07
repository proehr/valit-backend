package com.edu.m7.feedback.model.entity;

import com.edu.m7.feedback.model.VisualizationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity(name = "ChoiceQuestion")
@DiscriminatorValue("CHOICE")
@NoArgsConstructor
public class ChoiceQuestion extends Question {

    @Enumerated(EnumType.STRING)
    @Column(name = "visualization_type")
    @Setter
    private VisualizationType visualizationType;

    @OneToMany(mappedBy = "question")
    private Set<QuestionChoice> choices = new LinkedHashSet<>();

}
