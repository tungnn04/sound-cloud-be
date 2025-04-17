package com.example.soundcloudbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "USER_ID", nullable = false)
    private Integer userId;

    @Column(name = "SONG_ID", nullable = false)
    private Integer songId;

    @Column(name = "LISTENED_AT")
    private Date listenedAt;

}