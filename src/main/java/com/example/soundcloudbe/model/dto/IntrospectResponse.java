package com.example.soundcloudbe.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IntrospectResponse {
    private boolean valid;
}
