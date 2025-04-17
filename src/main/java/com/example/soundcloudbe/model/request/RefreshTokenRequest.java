package com.example.soundcloudbe.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenRequest {
    private String refreshToken;
}
