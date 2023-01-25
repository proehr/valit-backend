package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.mapping.FeedbackUserDetailsMapper;
import com.edu.m7.feedback.model.repository.EvaluationRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService {
    private final EvaluationRepository repository;

    public EvaluationService(EvaluationRepository repository) {
        this.repository = repository;
    }


    public Evaluation loadEvaluationById(Long id) throws UsernameNotFoundException {
        return repository.findById(id).orElseThrow();
                       // .orElseThrow(() -> new UsernameNotFoundException("No user found with username = " + username))
        //);
    }
}
