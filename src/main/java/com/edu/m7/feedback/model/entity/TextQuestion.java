package com.edu.m7.feedback.model.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "TextQuestion")
@DiscriminatorValue("TEXT")
public class TextQuestion extends Question {
}
