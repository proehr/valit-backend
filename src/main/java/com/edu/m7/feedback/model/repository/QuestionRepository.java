package com.edu.m7.feedback.model.repository;

import com.edu.m7.feedback.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
