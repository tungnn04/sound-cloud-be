package com.example.soundcloudbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "ARTIST_ID")
    private Integer artistId;

    @Column(name = "ALBUM_ID")
    private Integer albumId;

    @Column(name = "CATEGORY_ID")
    private Integer categoryId;

    @Column(name = "DURATION")
    private Integer duration;

    @Column(name = "FILE_URL", nullable = false)
    private String fileUrl;

    @Column(name = "COVER_URL")
    private String coverUrl;

    @ColumnDefault("0")
    @Column(name = "PLAY_COUNT", nullable = false)
    private Integer playCount;

    @Column(name = "UPLOADED_BY")
    private Integer uploadedBy;

    @Column(name = "CREATED_AT")
    private Date createdAt;

}