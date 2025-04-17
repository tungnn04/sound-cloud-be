package com.example.soundcloudbe.repository;

import com.example.soundcloudbe.entity.MusicState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicStateRepository extends JpaRepository<MusicState, Integer> {
}