package com.edu.m7.feedback.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Lecturer {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long lecturerId;
    private String firstName;
    private String lastName;
    private String title;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account", referencedColumnName = "accountId")
    private Account account;
}
