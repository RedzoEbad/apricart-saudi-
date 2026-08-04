package com.apricart.consumer.utils;

import com.apricart.consumer.security.enums.ImageType;
import com.apricart.consumer.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUtils {
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    @Autowired
    private BaseService baseService;
    @Autowired
    private Environment environment;

    public static boolean isValidImageFileType(String fileType) {
        if (fileType == null) return false;
        try {
            ImageType.valueOf(fileType.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String upload(MultipartFile file, String fileName, String originalFilename, String fileType) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is null or empty");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name is invalid");
        }
        if (!isValidImageFileType(fileType)) {
            throw new IllegalArgumentException("Invalid file type: " + fileType);
        }

        try {
            String uploadFileName = isSpecialFilename(originalFilename, fileName)
                    ? fileName.toUpperCase() + "." + fileType.toLowerCase()
                    : fileName + "." + fileType.toLowerCase();

            Path copyLocation = Paths.get(getUploadDirectory(), uploadFileName).normalize();
            String downloadPath = getPathLocation() + uploadFileName;

            Files.createDirectories(copyLocation.getParent());
            if (!Files.isWritable(copyLocation.getParent())) {
                throw new IOException("Upload directory is not writable: " + copyLocation.getParent());
            }

            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Image uploaded successfully: {}", copyLocation);
            return downloadPath;
        } catch (IOException e) {
            log.error("Failed to upload image: fileName={}, originalFilename={}", fileName, originalFilename, e);
            throw new RuntimeException("Image upload failed", e);
        }
    }

    public String getUploadDirectory() {
        String dirPath = baseService.isNotLocal(environment)
                ? "/home/ubuntu/uploads/"
                : System.getProperty("user.home") + "/Uploads/";
        try {
            Path dir = Paths.get(dirPath).normalize();
            Files.createDirectories(dir);
            if (!Files.isWritable(dir)) {
                throw new IOException("Upload directory is not writable: " + dirPath);
            }
            log.info("Upload directory initialized: {}", dirPath);
            return dirPath;
        } catch (IOException e) {
            log.error("Failed to initialize upload directory: {}", dirPath, e);
            throw new RuntimeException("Upload directory setup failed", e);
        }
    }

    public String getPathLocation() {
        String baseUrl = environment.getProperty("server.consumer.baseurl", "http://localhost:8081");
        String path = baseService.isNotLocal(environment)
                ? "/options/stream/"
                : "/uploads/";
        log.info("Download path prefix: {}", path);
        return baseUrl + path;
    }

    private boolean isSpecialFilename(String originalFilename, String fileName) {
        return originalFilename != null && originalFilename.length() >= 3 && fileName != null &&
                Utilities.getFirst(originalFilename.toUpperCase(), 3).equalsIgnoreCase("APR") &&
                fileName.contains("-");
    }

    public String getImagePath(String imageUrl) {
        String baseUrl = environment.getProperty("server.consumer.baseurl", "http://localhost:8081");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://") || imageUrl.startsWith(baseUrl)) {
                return imageUrl;
            } else {
                return baseUrl + (imageUrl.startsWith("/") ? imageUrl : "/" + imageUrl);
            }
        }
        return "";
    }
}