package com.example.soundcloudbe.controller;

import com.example.soundcloudbe.model.dto.ApiResponse;
import com.example.soundcloudbe.model.dto.SongResponse;
import com.example.soundcloudbe.model.request.CreateSongRequest;
import com.example.soundcloudbe.model.request.SearchSongRequest;
import com.example.soundcloudbe.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/songs")
public class SongController {
    @Autowired
    private SongService songService;

    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(songService.getSong(id)))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<?> related(@PathVariable("id") Integer id) {

    }
    

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                    @RequestParam(defaultValue = "1000") @Positive Integer size,
                                    @RequestBody SearchSongRequest request) {
        Page<SongResponse> result = songService.findAll(request, PageRequest.of(page, size));
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(result.getContent()))
                .meta(ApiResponse.Meta.builder()
                        .total(result.getTotalElements())
                        .page(page + 1)
                        .pageOfNumber(result.getTotalPages())
                        .build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String title, @RequestParam(required = false) Integer artistId,
                                    @RequestParam(required = false) Integer albumId, @RequestParam(required = false) Integer categoryId,
                                    @RequestParam MultipartFile audio, @RequestParam MultipartFile coverImage) throws IOException {
        CreateSongRequest request = new CreateSongRequest();
        request.setTitle(title);
        request.setArtistId(artistId);
        request.setAlbumId(albumId);
        request.setCategoryId(categoryId);
        request.setAudio(audio);
        request.setCoverImage(coverImage);
        songService.create(request);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Song created successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        songService.delete(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Song deleted successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }


}
