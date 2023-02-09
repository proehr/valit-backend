package com.edu.m7.feedback.payload.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiveFeedbackMessage {

    private Long participant;
    private LiveFeedback messageType;

    public enum LiveFeedback{
        NEW_PARTICIPANT,
        PARTICIPANT_LEFT,
        AUDIO_OK,
        AUDIO_NOT_OK,
        SPEED_OK,
        SPEED_NOT_OKAY,
        SURVEY_A,
        SURVEY_B,
        SURVEY_C,
        SURVEY_D
    }
}
