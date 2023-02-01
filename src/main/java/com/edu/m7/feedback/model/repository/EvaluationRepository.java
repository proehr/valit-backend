package com.edu.m7.feedback.model.repository;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByCourseOrderByDateDesc(Course course);
    Evaluation findEvaluationByShortcode(Integer shortcode);
}
