package com.example.soundcloudbe.model.dto;

import com.example.soundcloudbe.entity.Album;
import lombok.Data;

import java.util.List;

@Data
public class ArtistResponse {
    private Integer id;
    private String name;
    private String profilePicture;
    private List<AlbumResponse> albums;
    private List<SongResponse> songs;
}
