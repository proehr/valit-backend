package com.edu.m7.feedback.model.repository;

import com.edu.m7.feedback.model.entity.QuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionChoiceRepository extends JpaRepository<QuestionChoice, Long> {
}
