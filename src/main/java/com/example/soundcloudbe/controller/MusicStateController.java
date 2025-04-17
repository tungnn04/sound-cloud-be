package com.example.soundcloudbe.controller;

import com.cloudinary.Api;
import com.example.soundcloudbe.entity.MusicState;
import com.example.soundcloudbe.model.dto.ApiResponse;
import com.example.soundcloudbe.model.dto.MusicStateDTO;
import com.example.soundcloudbe.service.MusicStateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/music-state")
public class MusicStateController {
    @Autowired
    private MusicStateService musicStateService;
    @Autowired
    private ObjectMapper mapper;

    @GetMapping
    public ResponseEntity<?> getMusicState() {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(musicStateService.getMusicState()))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveMusicState(@RequestBody MusicStateDTO dto) {
        musicStateService.saveMusicState(dto);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Save music state success")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
