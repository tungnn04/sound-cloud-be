package com.example.soundcloudbe.controller;

import com.example.soundcloudbe.model.dto.ApiResponse;
import com.example.soundcloudbe.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(categoryService.findAll()))
                .build();

        return  ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id){
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(categoryService.detail(id)))
                .build();

        return  ResponseEntity.ok(apiResponse);
    }
}
