package com.example.soundcloudbe.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateSongRequest {
    private String title;
    private Integer artistId;
    private Integer albumId;
    private Integer categoryId;
    private MultipartFile audio;
    private MultipartFile coverImage;
}
