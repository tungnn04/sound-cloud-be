package com.example.soundcloudbe.service;

import com.example.soundcloudbe.model.dto.SongResponse;

import java.util.List;

public interface FavoriteService {
    List<SongResponse> findAll(Boolean sortDesc);

    void addSong(Integer songId);

    void deleteSong(Integer songId);

}
