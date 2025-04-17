package com.example.soundcloudbe.repository;

import com.example.soundcloudbe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}