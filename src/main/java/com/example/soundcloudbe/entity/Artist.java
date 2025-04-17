package com.example.soundcloudbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "PROFILE_PICTURE")
    private String profilePicture;

    @Column(name = "CREATED_AT")
    private Date createdAt;

}