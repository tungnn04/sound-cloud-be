package com.example.soundcloudbe.model.request;

import lombok.Data;

@Data
public class VerifyOTPRequest {
    private String email;
    private String otp;
}
