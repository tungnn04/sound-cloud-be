package com.example.soundcloudbe.controller;


import com.cloudinary.Api;
import com.example.soundcloudbe.entity.History;
import com.example.soundcloudbe.model.dto.ApiResponse;
import com.example.soundcloudbe.model.request.HistoryRequest;
import com.example.soundcloudbe.service.HistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ObjectMapper mapper;

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                     @RequestParam(defaultValue = "1000") @Positive Integer size) {
        Page<History> result = historyService.getAllHistory(PageRequest.of(page, size));
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(result.getContent()))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody HistoryRequest request) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Saved history successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
