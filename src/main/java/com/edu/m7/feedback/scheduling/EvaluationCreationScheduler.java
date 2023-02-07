package com.edu.m7.feedback.scheduling;

import com.edu.m7.feedback.model.EvaluationType;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Date;
import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.repository.CourseRepository;
import com.edu.m7.feedback.model.repository.DateRepository;
import com.edu.m7.feedback.model.repository.EvaluationRepository;
import com.edu.m7.feedback.service.EvaluationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class EvaluationCreationScheduler {

    private final DateRepository dateRepository;
    private final EvaluationService evaluationService;
    private final EvaluationRepository evaluationRepository;
    private final CourseRepository courseRepository;

    @Value("${valit.app.evaluationCreationOffsetMins}")
    private int evaluationCreationOffsetMins;

    public EvaluationCreationScheduler(
            DateRepository dateRepository,
            EvaluationService evaluationService,
            EvaluationRepository evaluationRepository,
            CourseRepository courseRepository) {
        this.dateRepository = dateRepository;
        this.evaluationService = evaluationService;
        this.evaluationRepository = evaluationRepository;
        this.courseRepository = courseRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void createCurrentEvaluations() {
        LocalTime offsetTime = LocalTime.now().plus(evaluationCreationOffsetMins, ChronoUnit.MINUTES);

        List<Date> datesToday = dateRepository.findAllByLocalDate(LocalDate.now());
        for (Date date : datesToday) {
            boolean hasActiveEvaluation = date
                    .getCourse()
                    .getEvaluations()
                    .stream()
                    .anyMatch((Evaluation evaluation) -> evaluation.isActive()
                            && evaluation.getType() == EvaluationType.REGULAR);
            if (!hasActiveEvaluation &&
                    offsetTime.isAfter(date.getCourse().getTimeStart()) &&
                    LocalTime.now().isBefore(date.getCourse().getTimeEnd())
            ) {
                evaluationService.createEvaluation(date.getCourse(), date.getLocalDate(), EvaluationType.REGULAR);
            }
        }
    }

    @Transactional
    @Scheduled(fixedRate = 300_000)
    public void generateSemesterEvaluations() {
        List<Course> coursesWithFinalToday = courseRepository.findByFinalEvaluationDate(LocalDate.now());
        for (Course course : coursesWithFinalToday) {
            boolean hasActiveFinal = course.getEvaluations().stream()
                    .anyMatch(evaluation -> evaluation.isActive() && evaluation.getType() == EvaluationType.FINAL);
            if (!hasActiveFinal) {
                evaluationService.createEvaluation(course, course.getFinalEvaluationDate(), EvaluationType.FINAL);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void deactivateEvaluations() {
        List<Evaluation> activeEvaluations = evaluationRepository.findByActiveTrue();
        for (Evaluation activeEvaluation : activeEvaluations) {

            if (activeEvaluation.getType() == EvaluationType.REGULAR &&
                    LocalDate.now().isAfter(activeEvaluation.getDate())
            ) {
                activeEvaluation.setActive(false);
                activeEvaluation.setShortcode(null);
                evaluationRepository.save(activeEvaluation);
            }

            // TODO: for how long do we want to leave final evaluations open?
        }
    }

}
