package com.example.soundcloudbe.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IntrospectRequest {
    private String token;
}
