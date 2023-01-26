package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.dto.CourseDto;
import com.edu.m7.feedback.model.dto.EvaluationDto;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.EvaluationRepository;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.service.CourseService;
import com.edu.m7.feedback.service.EvaluationService;
import com.edu.m7.feedback.service.LecturerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {
    private final EvaluationService evaluationService;
    private final LecturerService lecturerService;
    private final CourseService courseService;

    private final EvaluationRepository evaluationRepository;

    public EvaluationController(EvaluationService evaluationService, LecturerService lecturerService, CourseService courseService, EvaluationRepository evaluationRepository) {
        this.evaluationService = evaluationService;
        this.lecturerService = lecturerService;
        this.courseService = courseService;
        this.evaluationRepository = evaluationRepository;
    }

    //Get questions for Student

    //Get Questions + Answers for Lecturer
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{id}/summary")
    ResponseEntity<?> getEvaluation(@PathVariable Long id, Principal principal) {
        //TODO secure this via principal
        Lecturer principalLecturer = lecturerService.getLecturer(principal);
        Course relatedCourse = evaluationService.getCourse(id);
        Lecturer lecturer = relatedCourse.getLecturer();

        if(!principalLecturer.equals(lecturer) ){
                return new ResponseEntity<>(
                        new MessageResponse("Access to the requested Evaluation is not allowed"), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(evaluationService.loadEvaluationById(id));
    }




}
