package com.edu.m7.feedback.model.repository;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
