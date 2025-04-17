package com.example.soundcloudbe.service;

import com.example.soundcloudbe.model.dto.SongResponse;

import java.util.List;

public interface FavoriteService {
    List<SongResponse> findAll();

    void addSong(Integer songId);

    void deleteSong(Integer songId);

}
