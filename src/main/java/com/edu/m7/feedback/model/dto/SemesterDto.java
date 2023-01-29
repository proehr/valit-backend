package com.edu.m7.feedback.model.dto;

import com.edu.m7.feedback.model.entity.Semester;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link Semester} entity
 */
@Data
public class SemesterDto implements Serializable {
    private final long semesterId;
    private final String semesterName;
    private final LocalDate startDate;
    private final LocalDate endDate;
}