package com.example.soundcloudbe.controller;

import com.example.soundcloudbe.model.dto.ApiResponse;
import com.example.soundcloudbe.model.dto.AuthenticationResponse;
import com.example.soundcloudbe.model.dto.IntrospectResponse;
import com.example.soundcloudbe.model.request.*;
import com.example.soundcloudbe.service.AuthenticationService;
import com.example.soundcloudbe.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ObjectMapper mapper;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse result = authenticationService.authenticate(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Login successful")
                .data(mapper.valueToTree(result))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserRequest request) {
        authenticationService.register(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("User registered successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) throws MessagingException {
        authenticationService.forgotPassword(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("OTP send successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authenticationService.resetPassword(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Reset password successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestBody VerifyOTPRequest request) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Verify OPT successfully")
                .data(mapper.valueToTree(authenticationService.verifyOTP(request)))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/introspect")
    public ResponseEntity<?> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(mapper.valueToTree(result))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        AuthenticationResponse result = authenticationService.refreshToken(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Refresh token successful")
                .data(mapper.valueToTree(result))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
