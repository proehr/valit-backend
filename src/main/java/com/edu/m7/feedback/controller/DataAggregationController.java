package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.service.CourseService;
import com.edu.m7.feedback.service.DataAggregationService;
import com.edu.m7.feedback.service.LecturerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/data")
@CrossOrigin
@Slf4j
public class DataAggregationController {

    private final DataAggregationService dataAggregationService;
    private final LecturerService lecturerService;
    private final CourseService courseService;

    public DataAggregationController(
            DataAggregationService dataAggregationService,
            LecturerService lecturerService,
            CourseService courseService
    ) {
        this.dataAggregationService = dataAggregationService;
        this.lecturerService = lecturerService;
        this.courseService = courseService;
    }

    @GetMapping("/rating")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_LECTURER"})
    ResponseEntity<Integer> getOverallCourseRating(Principal principal) {
        Lecturer lecturer = lecturerService.getLecturer(principal);
        return new ResponseEntity<>(dataAggregationService.getOverallCourseRating(lecturer), HttpStatus.OK);

    }

    @GetMapping("/{courseId}/attendance")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_LECTURER"})
    ResponseEntity<List<Integer>> getAttendance(@PathVariable Long courseId, Principal principal) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        Long courseLecturerId = courseService.getLecturerByCourseId(courseId);
        if (!courseLecturerId.equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(dataAggregationService.getAttendance(courseId), HttpStatus.OK);
    }
}
