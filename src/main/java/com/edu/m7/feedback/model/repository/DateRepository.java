package com.edu.m7.feedback.model.repository;

import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DateRepository extends JpaRepository<Date, Long> {

    List<Date> findAllByLocalDate(LocalDate date);

}
