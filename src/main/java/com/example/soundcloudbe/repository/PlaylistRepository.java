package com.example.soundcloudbe.repository;

import com.example.soundcloudbe.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Playlist findByName(String name);
    List<Playlist> findByUserIdOrderByCreatedAtAsc(Integer userId);
    List<Playlist> findByUserIdOrderByCreatedAtDesc(Integer userId);
}