package com.example.soundcloudbe.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MusicStateDTO {
    private Integer songId;
    private Integer position;
    private Boolean isPlaying;
}
