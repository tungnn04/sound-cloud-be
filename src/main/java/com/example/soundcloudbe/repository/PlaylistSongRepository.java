package com.example.soundcloudbe.repository;

import com.example.soundcloudbe.entity.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Integer> {
    List<PlaylistSong> findByPlaylistId(Integer playlistId);
    PlaylistSong findByPlaylistIdAndSongId(Integer playlistId, Integer songId);
}