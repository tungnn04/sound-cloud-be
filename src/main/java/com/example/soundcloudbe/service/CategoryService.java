package com.example.soundcloudbe.service;

import com.example.soundcloudbe.entity.Category;
import com.example.soundcloudbe.exception.AppException;
import com.example.soundcloudbe.exception.ErrorCode;
import com.example.soundcloudbe.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category detail(Integer id) {
        return categoryRepository.findById(id).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
    }
}
