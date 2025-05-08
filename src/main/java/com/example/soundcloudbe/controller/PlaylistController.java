package com.example.soundcloudbe.controller;

import com.example.soundcloudbe.model.dto.ApiResponse;
import com.example.soundcloudbe.model.dto.PlaylistDTO;
import com.example.soundcloudbe.model.request.PlaylistRequest;
import com.example.soundcloudbe.service.PlaylistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<PlaylistDTO> list = playlistService.findAll();
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(list))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        PlaylistDTO playlistDTO = playlistService.detail(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(playlistDTO))
                .build();
        return  ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPlaylist(@RequestBody @Valid PlaylistRequest dto) {
        playlistService.createPlaylist(dto);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Create playlist successful")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updatePlaylist(@PathVariable("id") Integer id, @RequestBody PlaylistRequest dto) {
        playlistService.updatePlaylist(id,dto);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Update playlist successful")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{playlistId}/addSong/{songId}")
    public ResponseEntity<?> addSong(@PathVariable("playlistId") Integer playlistId, @PathVariable("songId") Integer songId) {
        playlistService.addSong(playlistId,songId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Add song to playlist successful")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{playlistId}/deleteSong/{songId}")
    public ResponseEntity<?> deleteSong(@PathVariable("playlistId") Integer playlistId, @PathVariable("songId") Integer songId) {
        playlistService.deleteSong(playlistId,songId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Delete song in playlist successful")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable("playlistId") Integer playlistId) {
        playlistService.delete(playlistId);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Delete playlist successful")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
