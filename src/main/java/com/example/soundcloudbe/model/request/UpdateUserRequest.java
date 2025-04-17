package com.example.soundcloudbe.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {
    @NotEmpty
    private String fullName;
    private MultipartFile avatarImage;
}
