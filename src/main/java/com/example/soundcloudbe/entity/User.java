package com.example.soundcloudbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    @Column(name = "FULL_NAME", length = 100)
    private String fullName;

    @Column(name = "AVATAR_URL")
    private String avatarUrl;

    @Column(name = "VERIFICATION_CODE")
    private String verificationCode;

    @Column(name = "CREATED_AT")
    private Date createdAt;

}