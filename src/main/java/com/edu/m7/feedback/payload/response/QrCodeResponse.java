package com.edu.m7.feedback.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QrCodeResponse {
    private byte[] byteArray;
    private Integer shortcode;
}
