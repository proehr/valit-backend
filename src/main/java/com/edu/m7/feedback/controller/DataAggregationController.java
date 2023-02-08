package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.service.DataAggregationService;
import com.edu.m7.feedback.service.LecturerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/data")
@CrossOrigin
@Slf4j
public class DataAggregationController {

    private final DataAggregationService dataAggregationService;
    private final LecturerService lecturerService;

    public DataAggregationController(DataAggregationService dataAggregationService, LecturerService lecturerService) {
        this.dataAggregationService = dataAggregationService;
        this.lecturerService = lecturerService;
    }

    @GetMapping("/rating")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_LECTURER"})
    ResponseEntity<Integer> getOverallCourseRating(Principal principal){
        Lecturer lecturer = lecturerService.getLecturer(principal);
        try{
            return new ResponseEntity<>(dataAggregationService.getOverallCourseRating(lecturer), HttpStatus.OK);
        }catch (NoSuchElementException e){
            log.info("Requested course rating for lecturer without ratings", e);
            return new ResponseEntity<>(0, HttpStatus.NO_CONTENT);
        }

    }
}
