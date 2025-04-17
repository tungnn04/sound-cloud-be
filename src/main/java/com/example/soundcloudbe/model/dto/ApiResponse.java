package com.example.soundcloudbe.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private int code;
    private String message;
    private JsonNode data;
    private Meta meta;
    @Data
    @Builder
    public static class Meta {
        long total;

        int page;

        @JsonProperty("page_of_number")
        int pageOfNumber;
    }
}
