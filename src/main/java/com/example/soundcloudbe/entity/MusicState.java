package com.example.soundcloudbe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "music_state")
public class MusicState {
    @Id
    @Column(name = "USER_ID", nullable = false)
    private Integer id;

    @Column(name = "SONG_ID", nullable = false)
    private Integer songId;

    @ColumnDefault("0")
    @Column(name = "POSITION", nullable = false)
    private Integer position;

    @ColumnDefault("0")
    @Column(name = "IS_PLAYING", nullable = false)
    private Boolean isPlaying = false;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

}