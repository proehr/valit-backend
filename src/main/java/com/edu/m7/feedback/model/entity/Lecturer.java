package com.edu.m7.feedback.model.entity;

import javax.persistence.*;

@Entity
public class Lecturer {

    private String firstName;
    private String lastName;
    private String title;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long lecturerId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account", referencedColumnName = "accountId")
    private Account account;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(long lecturerId) {
        this.lecturerId = lecturerId;
    }

}
