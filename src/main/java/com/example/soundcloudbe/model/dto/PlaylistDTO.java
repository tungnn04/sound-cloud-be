package com.example.soundcloudbe.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaylistDTO {
    private Integer id;
    private String name;
    private Integer userId;
    private String coverUrl;

    private List<SongResponse> songs;
}
