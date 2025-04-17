package com.example.soundcloudbe.service;

import com.example.soundcloudbe.model.dto.SongResponse;

import java.util.List;

public interface PlaylistSongService {
    List<SongResponse> findSongByPlaylistId(Integer playlistId);
}
