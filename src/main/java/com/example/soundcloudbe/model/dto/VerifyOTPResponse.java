package com.example.soundcloudbe.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyOTPResponse {
    private String email;
    private String token;
}
