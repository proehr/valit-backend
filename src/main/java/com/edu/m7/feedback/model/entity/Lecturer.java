package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Lecturer {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long lecturerId;

    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String title;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account", referencedColumnName = "accountId")
    private Account account;

    @OneToMany(mappedBy = "lecturer")
    private Set<Course> courses = new LinkedHashSet<>();
}
