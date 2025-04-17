package com.example.soundcloudbe.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.soundcloudbe.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String path, Boolean isImage) throws IOException {
        long startTime = System.currentTimeMillis();
        try {

            String resourceType = (isImage) ? "image" : "video";
            String filename = removeExtension(file.getOriginalFilename());
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", resourceType,
                    "folder", path,
                    "public_id", filename
            ));
            long endTime = System.currentTimeMillis();
            log.info("Upload completed in {} ms", (endTime - startTime));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String url) {
        if (url == null) {
            return;
        }
        String publicId = DataUtil.getPublicIdFromUrl(url);
        String resourceType = DataUtil.getResourceTypeFromUrl(url);
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                    "resource_type", resourceType
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String removeExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }
        int lastIndexOfDot = filename.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return filename;
        }
        return filename.substring(0, lastIndexOfDot);
    }

}
