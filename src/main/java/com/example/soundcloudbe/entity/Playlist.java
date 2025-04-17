package com.example.soundcloudbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "USER_ID", nullable = false)
    private Integer userId;

    @Column(name = "COVER_URL")
    private String coverUrl;

    @Column(name = "CREATED_AT")
    private Date createdAt;

}