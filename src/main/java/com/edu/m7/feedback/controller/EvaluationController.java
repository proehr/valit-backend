package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.dto.AnswerDto;
import com.edu.m7.feedback.model.dto.QuestionDto;
import com.edu.m7.feedback.model.entity.Question;
import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import com.edu.m7.feedback.service.EvaluationService;
import com.edu.m7.feedback.service.LecturerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/evaluations")
@Slf4j
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
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        return ResponseEntity.ok(evaluationService.loadEvaluationById(id));
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
    @GetMapping("/questions")
    ResponseEntity<EvaluationResponseDto> getQuestions(@RequestBody Integer shortcode) {
        //TODO: Wenn antowrten schon abgegeben wurden, werden die trotzdem noch übertrageen
        EvaluationResponseDto evaluationResponseDto = evaluationService.getEvaluationByShortcode(shortcode);
        return ResponseEntity.ok(evaluationResponseDto);
    }


}
