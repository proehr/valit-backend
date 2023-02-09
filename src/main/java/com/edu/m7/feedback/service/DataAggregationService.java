package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.EvaluationType;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.entity.IntAnswer;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.CourseRepository;
import com.edu.m7.feedback.payload.response.AttendanceResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DataAggregationService {

    private static final int QUESTION_CHOICE_AMOUNT = 5;
    private static final int[] CHOICE_WEIGHTS = new int[]{0, 25, 50, 75, 100};

    private final CourseRepository courseRepository;

    public DataAggregationService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public int getOverallCourseRating(Lecturer lecturer) {

        int[] answerCountArray = new int[QUESTION_CHOICE_AMOUNT];
        lecturer.getCourses().stream()
                .flatMap(course -> course.getEvaluations().stream())
                .flatMap(evaluation -> evaluation.getQuestions().stream())
                .filter(question -> question.getQuestionPosition() == 0 && question.getSectionNumber() == 0)
                .flatMap(question -> question.getAnswers().stream())
                .forEach(answer -> answerCountArray[((IntAnswer) answer).getValue()]++);
        int answerAmount = 0;
        int rating = 0;
        for (int i = 0; i < answerCountArray.length; i++) {
            answerAmount += answerCountArray[i];
            rating += CHOICE_WEIGHTS[i] * answerCountArray[i];
        }

        if (answerAmount == 0) {
            return -1;
        }

        return rating / answerAmount;

    }

    public List<AttendanceResponse> getAttendance(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        List<AttendanceResponse> attendanceList = new ArrayList<>();
        for (Evaluation evaluation : course.getEvaluations()) {
            if (evaluation.getType() == EvaluationType.REGULAR) {
                Integer size = evaluation.getQuestions().iterator().next().getAnswers().size();
                attendanceList.add(new AttendanceResponse(size, evaluation.getDate()));
            }
        }
        Collections.reverse(attendanceList);
        return attendanceList;
    }
}
