package com.example.soundcloudbe.controller;

import com.example.soundcloudbe.model.dto.ApiResponse;
import com.example.soundcloudbe.model.request.UpdateUserRequest;
import com.example.soundcloudbe.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/getInfo")
    public ResponseEntity<?> getUser() {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Get user information successfully")
                .data(mapper.valueToTree(userService.getUser()))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam String fullName, @RequestParam(required = false) MultipartFile avatarImage) throws IOException {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFullName(fullName);
        request.setAvatarImage(avatarImage);
        userService.updateUser(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Update profile successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getUpload")
    public ResponseEntity<?> getUserUpload() {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .data(mapper.valueToTree(userService.getUserUpload()))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
