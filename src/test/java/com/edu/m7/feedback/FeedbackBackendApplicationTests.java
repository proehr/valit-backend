package com.edu.m7.feedback;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class FeedbackBackendApplicationTests {

    @Test
    void testContextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
    }

}
