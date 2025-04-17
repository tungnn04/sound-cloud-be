package com.example.soundcloudbe.repository;

import com.example.soundcloudbe.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
    List<Album> findByArtistId(Integer artistId);
}