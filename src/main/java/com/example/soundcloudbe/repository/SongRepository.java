package com.example.soundcloudbe.repository;

import com.example.soundcloudbe.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Integer> {
}