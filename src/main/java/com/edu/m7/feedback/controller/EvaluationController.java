package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import com.edu.m7.feedback.payload.response.QuestionResponseDto;
import com.edu.m7.feedback.payload.response.EvaluationHeaderResponse;
import com.edu.m7.feedback.service.EvaluationService;
import com.edu.m7.feedback.service.LecturerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/evaluations")
@Slf4j
@CrossOrigin
public class EvaluationController {
    private final EvaluationService evaluationService;
    private final LecturerService lecturerService;


    public EvaluationController(EvaluationService evaluationService, LecturerService lecturerService) {
        this.evaluationService = evaluationService;
        this.lecturerService = lecturerService;
    }

    //Get questions for Student

    //Get Questions + Answers for Lecturer
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{id}/summary")
    ResponseEntity<EvaluationResponseDto> getEvaluation(@PathVariable Long id, Principal principal) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!evaluationService.getLecturerIdByEvaluationId(id).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            EvaluationResponseDto evaluationResponseDto = evaluationService.loadEvaluationById(id);
            return ResponseEntity.ok(evaluationResponseDto);
        } catch (NoSuchElementException e) {
            log.info("Could not find evaluation ", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{id}/participant/{participantId}")
    ResponseEntity<EvaluationResponseDto> getEvaluationResultByParticipant(
            @PathVariable Long id,
            @PathVariable Long participantId,
            Principal principal) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!evaluationService.getLecturerIdByEvaluationId(id).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        try {
            EvaluationResponseDto evaluationResponseDto = evaluationService.loadEvaluationResultByParticipant(id, participantId);
            return ResponseEntity.ok(evaluationResponseDto);
        } catch (NoSuchElementException e) {
            log.info("Could not find evaluation result by participant", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{id}/participants")
    ResponseEntity<List<Long>> getParticipants(@PathVariable Long id, Principal principal) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!evaluationService.getLecturerIdByEvaluationId(id).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        try {
            List<Long> participants = evaluationService.getParticipants(id);
            return ResponseEntity.ok(participants);
        } catch (NoSuchElementException e) {
            log.info("Could not find participants of evaluation", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RolesAllowed({"ROLE_STUDENT", "ROLE_ADMIN"})
    @GetMapping("/{shortCode}/questions")
    ResponseEntity<List<QuestionResponseDto>> getQuestions(@PathVariable Integer shortCode) {
        try {
            List<QuestionResponseDto> questions = evaluationService.getQuestions(shortCode);
            return ResponseEntity.ok(questions);
        } catch (NoSuchElementException e) {
            log.info(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("{courseId}/{id}/header")
    ResponseEntity<EvaluationHeaderResponse> getEvaluationHeader(
            @PathVariable Long id,
            @PathVariable Long courseId,
            Principal principal
    ) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!evaluationService.getLecturerIdByEvaluationId(id).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            EvaluationHeaderResponse evaluationHeaderResponse =
                    evaluationService.loadEvaluationHeaderById(id, courseId);
            return ResponseEntity.ok(evaluationHeaderResponse);
        } catch (NoSuchElementException e) {
            log.info("Could not find evaluation header", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
