package com.example.soundcloudbe.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSongRequest {
    private String title;
    private Integer categoryId;
}
