package com.example.soundcloudbe.repository;

import com.example.soundcloudbe.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Optional<Favorite> findByUserIdAndSongId(Integer userId, Integer songId);
}