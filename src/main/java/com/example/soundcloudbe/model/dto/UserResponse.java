package com.example.soundcloudbe.model.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Integer id;

    private String email;

    private String fullName;

    private String avatarUrl;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
//    private Date createdAt;
}
