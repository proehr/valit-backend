package com.edu.m7.feedback.model.entity;

import com.edu.m7.feedback.model.EvaluationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "evaluation")
@NoArgsConstructor
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id", nullable = false)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_fk")
    private Course course;

    @Setter
    @Column(name = "type", columnDefinition = "evaluation_type")
    @Enumerated(EnumType.STRING)
    private EvaluationType type;

    @Setter
    @Column(name = "title")
    private String title;

    @Setter
    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "evaluation")
    @OrderBy("sectionNumber, questionPosition")
    private Set<Question> questions = new LinkedHashSet<>();

}
