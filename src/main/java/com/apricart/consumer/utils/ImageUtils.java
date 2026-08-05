package com.apricart.consumer.utils;

import com.apricart.consumer.security.enums.ImageType;
import com.apricart.consumer.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
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

    private final RestTemplate restTemplate = new RestTemplate();

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

        String uploadFileName = isSpecialFilename(originalFilename, fileName)
                ? fileName.toUpperCase() + "." + fileType.toLowerCase()
                : sanitizeFileName(fileName) + "." + fileType.toLowerCase();

        String objectKey = "products/" + uploadFileName;

        try {
            if (isSupabaseConfigured()) {
                return uploadToSupabase(file.getBytes(), objectKey, fileType);
            }
            log.warn("Supabase Storage not configured (missing SUPABASE_SERVICE_ROLE_KEY) — falling back to local filesystem");
            return uploadToLocalFilesystem(file, uploadFileName);
        } catch (IOException e) {
            log.error("Failed to upload image: fileName={}, originalFilename={}", fileName, originalFilename, e);
            throw new RuntimeException("Image upload failed", e);
        }
    }

    private boolean isSupabaseConfigured() {
        String key = environment.getProperty("supabase.service-role-key", "");
        String url = environment.getProperty("supabase.url", "");
        return key != null && !key.trim().isEmpty() && url != null && !url.trim().isEmpty();
    }

    private String uploadToSupabase(byte[] bytes, String objectKey, String fileType) {
        String baseUrl = trimTrailingSlash(environment.getProperty("supabase.url"));
        String bucket = environment.getProperty("supabase.storage.bucket", "apri");
        String serviceKey = environment.getProperty("supabase.service-role-key");

        String uploadUrl = baseUrl + "/storage/v1/object/" + bucket + "/" + objectKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceKey);
        headers.set("apikey", serviceKey);
        headers.set("x-upsert", "true");
        headers.setContentType(mediaTypeFor(fileType));

        HttpEntity<byte[]> entity = new HttpEntity<>(bytes, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl, HttpMethod.POST, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Supabase upload failed: HTTP " + response.getStatusCodeValue()
                        + " body=" + response.getBody());
            }
            String publicUrl = baseUrl + "/storage/v1/object/public/" + bucket + "/" + objectKey;
            log.info("Image uploaded to Supabase: {}", publicUrl);
            return publicUrl;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Supabase upload error for key={}", objectKey, e);
            throw new RuntimeException("Supabase image upload failed", e);
        }
    }

    private String uploadToLocalFilesystem(MultipartFile file, String uploadFileName) throws IOException {
        Path uploadRoot = Paths.get(getUploadDirectory()).normalize();
        Path copyLocation = uploadRoot.resolve(uploadFileName).normalize();
        if (!copyLocation.startsWith(uploadRoot)) {
            throw new IOException("Invalid upload path: " + copyLocation);
        }
        Files.createDirectories(uploadRoot);
        if (!Files.isWritable(uploadRoot)) {
            throw new IOException("Upload directory is not writable: " + uploadRoot);
        }
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        String downloadPath = getPathLocation() + uploadFileName;
        log.info("Image uploaded successfully (local): {}", copyLocation);
        return downloadPath;
    }

    private static MediaType mediaTypeFor(String fileType) {
        if (fileType == null) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        switch (fileType.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "webp":
                return MediaType.parseMediaType("image/webp");
            case "svg":
                return MediaType.parseMediaType("image/svg+xml");
            case "bmp":
                return MediaType.parseMediaType("image/bmp");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private static String trimTrailingSlash(String url) {
        if (url == null) return "";
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
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

    /** Keep only safe filename characters for storage object keys. */
    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "image-" + System.currentTimeMillis();
        }
        String cleaned = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
        cleaned = cleaned.replaceAll("_+", "_");
        return cleaned.isEmpty() ? "image-" + System.currentTimeMillis() : cleaned;
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
