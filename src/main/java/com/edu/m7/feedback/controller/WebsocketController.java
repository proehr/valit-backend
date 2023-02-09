package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.payload.websocket.LiveFeedbackMessage;
import com.edu.m7.feedback.service.AccountService;
import com.edu.m7.feedback.service.EvaluationService;
import com.edu.m7.feedback.service.LecturerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WebsocketController {

    private final AccountService accountService;
    private final SimpMessagingTemplate messagingTemplate;
    private final EvaluationService evaluationService;
    private final LecturerService lecturerService;

    public WebsocketController(
            AccountService accountService,
            SimpMessagingTemplate messagingTemplate,
            EvaluationService evaluationService,
            LecturerService lecturerService
    ) {
        this.accountService = accountService;
        this.messagingTemplate = messagingTemplate;
        this.evaluationService = evaluationService;
        this.lecturerService = lecturerService;
    }

    @PostMapping("/live/{shortCode}")
    @RolesAllowed("ROLE_STUDENT")
    public ResponseEntity<MessageResponse> sendFeedback(
            @RequestBody LiveFeedbackMessage message,
            @PathVariable String shortCode,
            Principal principal
    ) {
        Long account = accountService.getAccountIdByUsername(principal.getName());
        message.setParticipant(account);
        messagingTemplate.convertAndSend("/queue/" + shortCode, message);
        return new ResponseEntity<>(new MessageResponse("Sent message."), HttpStatus.OK);
    }

    @PostMapping("/live/{shortCode}/reset-survey")
    @RolesAllowed("ROLE_LECTURER")
    public ResponseEntity<MessageResponse> resetSurvey(@PathVariable String shortCode, Principal principal) {
        EvaluationResponseDto evaluationResponseDto = evaluationService.getEvaluationByShortcode(shortCode);
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!evaluationService.getLecturerIdByEvaluationId(evaluationResponseDto.getId()).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        messagingTemplate.convertAndSend("/topic/" + shortCode, "RESET_SURVEY");
        return new ResponseEntity<>(new MessageResponse("Survey reset message has been sent."), HttpStatus.OK);
    }

}
