package com.edu.m7.feedback.model.repository;

import com.edu.m7.feedback.model.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}