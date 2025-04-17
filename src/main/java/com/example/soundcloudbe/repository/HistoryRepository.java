package com.example.soundcloudbe.repository;

import com.example.soundcloudbe.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Integer> {
    Page<History> findByUserId(Integer userId, Pageable pageable);
}