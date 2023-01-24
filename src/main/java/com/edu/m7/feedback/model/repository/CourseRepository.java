package com.edu.m7.feedback.model.repository;

import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByLecturer(Lecturer lecturer);

}
