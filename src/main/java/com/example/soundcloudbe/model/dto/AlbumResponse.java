package com.example.soundcloudbe.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AlbumResponse {
    private Integer id;
    private String title;
    private String artistName;
    private String coverUrl;
    private Integer releaseYear;
    private List<SongResponse> songs;
}
