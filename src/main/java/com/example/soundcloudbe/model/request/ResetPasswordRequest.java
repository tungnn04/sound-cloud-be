package com.example.soundcloudbe.model.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String newPassword;
    private String confirmPassword;
}
