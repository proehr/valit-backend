package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.payload.request.PostAnswerRequest;
import com.edu.m7.feedback.payload.response.EvaluationHeaderResponse;
import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.payload.response.StudentEvaluationResponse;
import com.edu.m7.feedback.service.AccountService;
import com.edu.m7.feedback.service.EvaluationService;
import com.edu.m7.feedback.service.LecturerService;
import com.edu.m7.feedback.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final QuestionService questionService;
    private final AccountService accountService;


    public EvaluationController(
            EvaluationService evaluationService,
            LecturerService lecturerService,
            QuestionService questionService,
            AccountService accountService) {
        this.evaluationService = evaluationService;
        this.lecturerService = lecturerService;
        this.questionService = questionService;
        this.accountService = accountService;
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
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
    ResponseEntity<StudentEvaluationResponse> getQuestions(@PathVariable String shortCode) {
        try {
            StudentEvaluationResponse response = questionService.getQuestionsByEvaluationShortCode(shortCode);
            return ResponseEntity.ok(response);
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
                    evaluationService.loadEvaluationHeaderById(id);
            return ResponseEntity.ok(evaluationHeaderResponse);
        } catch (NoSuchElementException e) {
            log.info("Could not find evaluation header", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PostMapping("/{evaluationId}/update-title")
    ResponseEntity<EvaluationResponseDto> updateEvaluationTitle(
            @PathVariable Long evaluationId,
            @RequestBody String newTitle,
            Principal principal
    ) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!evaluationService.getLecturerIdByEvaluationId(evaluationId).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            EvaluationResponseDto evaluationResponseDto = evaluationService.updateTitle(evaluationId, newTitle);
            return ResponseEntity.ok(evaluationResponseDto);
        } catch (NoSuchElementException e) {
            log.info(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RolesAllowed({"ROLE_STUDENT", "ROLE_ADMIN"})
    @PostMapping("/{shortcode}/post-answers")
        //post answers for evaluation
    ResponseEntity<MessageResponse> postAnswers(@PathVariable String shortcode, @RequestBody List<PostAnswerRequest> postAnswerRequests, Principal principal) {
        //Check if evaluation exists
        EvaluationResponseDto evaluation;
        try {
            evaluation = evaluationService.getEvaluationByShortcode(shortcode);
        } catch (NoSuchElementException e) {
            log.info(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Long> participants = evaluationService.getParticipants(evaluation.getId());
        //check if student already sent answer for evaluation
        Account account = accountService.findByUsername(principal.getName());
        if (participants.contains(account.getAccountId())) {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        evaluationService.postAnswers(postAnswerRequests, account);
        return ResponseEntity.ok(new MessageResponse("Answers sent!"));
    }
}
