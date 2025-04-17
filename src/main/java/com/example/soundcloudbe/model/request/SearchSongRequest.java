package com.example.soundcloudbe.model.request;

import lombok.Data;

@Data
public class SearchSongRequest {
    private String title;
    private Integer categoryId;
}
