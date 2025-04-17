package com.example.soundcloudbe.controller;

import com.example.soundcloudbe.model.dto.ApiResponse;
import com.example.soundcloudbe.service.FavoriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(favoriteService.findAll()))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/addSong/{id}")
    public ResponseEntity<?> add(@PathVariable("id") Integer id) {
        favoriteService.addSong(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Add song to favorite successful")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/deleteSong/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        favoriteService.deleteSong(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Delete song in favorite successful")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
