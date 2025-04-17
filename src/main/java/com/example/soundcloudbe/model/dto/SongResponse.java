package com.example.soundcloudbe.model.dto;

import lombok.Data;

@Data
public class SongResponse {
    private Integer id;
    private String title;
    private String artistName;
    private String albumName;
    private String categoryName;
    private Integer duration;
    private String fileUrl;
    private String coverUrl;
    private Integer playCount;
    private Boolean isFavorite;
}
