package com.edu.m7.feedback.model.repository;

import com.edu.m7.feedback.model.entity.FileData;
import com.edu.m7.feedback.model.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    Optional<FileData> findByName(String fileName);

    Optional<FileData> findByLecturer(Lecturer lecturer);

}
