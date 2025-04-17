package com.example.soundcloudbe.util;

import java.util.Arrays;

public class DataUtil {
    public static boolean isNullOrZero(String value){
        return value == null || value.trim().isEmpty();
    }
    public static boolean isNullOrZero(Integer value){
        return value == null || value == 0;
    }

    public static String getPublicIdFromUrl(String url) {
        try {
            String[] parts = url.split("/");
            int uploadIndex = Arrays.asList(parts).indexOf("upload");

            if (uploadIndex == -1 || uploadIndex + 2 >= parts.length) {
                throw new IllegalArgumentException("URL không hợp lệ");
            }

            String publicIdWithExt = String.join("/", Arrays.copyOfRange(parts, uploadIndex + 2, parts.length));
            return publicIdWithExt.replaceAll("\\.[^.]+$", ""); // Xóa phần mở rộng .mp3, .jpg...
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy public_id: " + e.getMessage());
        }
    }

    public static String getResourceTypeFromUrl(String url) {
        if (url.contains("/image/upload/")) {
            return "image";
        } else if (url.contains("/video/upload/")) {
            return "video";
        } else {
            return "raw";
        }
    }
}
