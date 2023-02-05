package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.dto.SemesterDto;
import com.edu.m7.feedback.model.mapping.SemesterMapper;
import com.edu.m7.feedback.model.repository.SemesterRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/semester")
public class SemesterController {

    private final SemesterMapper mapper = Mappers.getMapper(SemesterMapper.class);

    private final SemesterRepository semesterRepository;

    public SemesterController(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    @GetMapping
    @RolesAllowed({"ROLE_ADMIN", "ROLE_LECTURER"})
    public ResponseEntity<List<SemesterDto>> getAllSemesterOptions() {
        return new ResponseEntity<>(semesterRepository
                .findAll()
                .stream()
                .map(mapper::semesterToSemesterDto)
                .collect(Collectors.toList()),
        HttpStatus.OK);
    }
}
