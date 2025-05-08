package com.example.soundcloudbe.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlaylistRequest {
    @NotBlank
    private String name;
}
