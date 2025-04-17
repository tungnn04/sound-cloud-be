package com.example.soundcloudbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Column(name = "ARTIST_ID", nullable = false)
    private Integer artistId;

    @Column(name = "COVER_URL")
    private String coverUrl;

    @Column(name = "RELEASE_DATE")
    private LocalDate releaseDate;

    @Column(name = "CREATED_AT")
    private Date createdAt;

}