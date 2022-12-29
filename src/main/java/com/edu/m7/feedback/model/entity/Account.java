package com.edu.m7.feedback.model.entity;

import com.edu.m7.feedback.model.AccountType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.validation.constraints.Email;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountId")
    private Long accountId;

    @Email
    @Column(name = "email")
    @Setter
    private String username;

    @Column(name = "password_hash")
    @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    @Setter
    private AccountType type;

    @CreationTimestamp
    private Timestamp createdAt;

    public Account(String username, String password, AccountType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

}
