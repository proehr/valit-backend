package com.edu.m7.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FeedbackBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedbackBackendApplication.class, args);
    }

}
