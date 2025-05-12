package com.example.soundcloudbe.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class HistoryRequest {
    private Integer songId;
    private Integer position;
}
