package com.edu.m7.feedback.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Table(name = "FILE_DATA")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    private String name;
    @Setter
    private String type;
    @Setter
    private String filePath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_fk")
    @Setter
    private Lecturer lecturer;
}
