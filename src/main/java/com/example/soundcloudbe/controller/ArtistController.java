package com.example.soundcloudbe.controller;

import com.example.soundcloudbe.model.dto.ApiResponse;
import com.example.soundcloudbe.model.dto.ArtistResponse;
import com.example.soundcloudbe.model.request.SearchArtistRequest;
import com.example.soundcloudbe.service.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artists")
public class ArtistController {
    @Autowired
    private ArtistService artistService;
    @Autowired
    private ObjectMapper mapper;

    @PostMapping("/search")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                     @RequestParam(defaultValue = "1000") @Positive Integer size,
                                     @RequestBody SearchArtistRequest request) {
        Page<ArtistResponse> result = artistService.findAll(PageRequest.of(page, size), request);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(result.getContent()))
                .meta(ApiResponse.Meta.builder()
                        .total(result.getTotalElements())
                        .page(page + 1)
                        .pageOfNumber(result.getTotalPages())
                        .build())
                .build();
        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(artistService.detail(id)))
                .build();
        return ResponseEntity.ok().body(apiResponse);
    }
}
