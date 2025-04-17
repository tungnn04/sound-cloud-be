package com.example.soundcloudbe.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PlaylistRequest {
    @NotBlank
    private String name;
    private MultipartFile coverImage;
}
