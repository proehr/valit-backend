package com.edu.m7.feedback.model.repository;

import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByCourseOrderByDateDesc(Course course);
    Evaluation findEvaluationByShortcode(String shortCode);

    List<Evaluation> findByActiveTrue();

    boolean existsByShortcode(String shortCode);

}
