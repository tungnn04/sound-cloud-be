package com.example.soundcloudbe.repository;

import com.example.soundcloudbe.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
}