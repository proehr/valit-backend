package com.edu.m7.feedback.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public abstract class AnswerDto {
    private Long account;
    private Instant createdAt;
}
