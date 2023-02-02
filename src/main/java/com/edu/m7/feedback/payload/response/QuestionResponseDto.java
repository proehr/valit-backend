package com.edu.m7.feedback.payload.response;

import com.edu.m7.feedback.model.QuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionResponseDto {

        private Long id;
        private String questionKey;
        private Integer questionPosition;
        private Integer sectionNumber;
        private QuestionType questionType;

}
