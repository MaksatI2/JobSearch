package org.example.JobSearch.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@UtilityClass
public class FileUtil {
    private static final String UPLOAD_DIR = "data/";
    public static final String DEFAULT_AVATAR = "default_avatar.jpg";
    public static final String IMAGES_SUBDIR = "images/";
    public static final String DEFAULT_AVATAR_DIR = "static/default-avatar/";

    @SneakyThrows
    public static String saveUploadFile(MultipartFile file, String subDir) {
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName = uuidFile + "_" + file.getOriginalFilename();

        Path pathDir = Paths.get(UPLOAD_DIR + subDir);
        Files.createDirectories(pathDir);

        Path filePath = pathDir.resolve(resultFileName);
        try (OutputStream os = Files.newOutputStream(filePath)) {
            os.write(file.getBytes());
        }
        return resultFileName;
    }

    @SneakyThrows
    public static ResponseEntity<?> getOutputFile(String avatarPath, MediaType mediaType) {
        if (avatarPath == null || avatarPath.isEmpty() || avatarPath.equals(DEFAULT_AVATAR)) {
            try {
                Resource resource = new ClassPathResource(DEFAULT_AVATAR_DIR + DEFAULT_AVATAR);
                if (resource.exists()) {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + DEFAULT_AVATAR + "\"")
                            .contentLength(resource.contentLength())
                            .contentType(mediaType)
                            .body(new ByteArrayResource(resource.getInputStream().readAllBytes()));
                }
            } catch (Exception e) {
                log.error("Default avatar loading error", e);
            }
        } else {
            Path filePath = Paths.get(UPLOAD_DIR + IMAGES_SUBDIR).resolve(avatarPath);
            if (Files.exists(filePath)) {
                byte[] fileBytes = Files.readAllBytes(filePath);
                Resource resource = new ByteArrayResource(fileBytes);
                String filename = filePath.getFileName().toString();

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .contentLength(resource.contentLength())
                        .contentType(mediaType)
                        .body(resource);
            }
        }

        try {
            Resource resource = new ClassPathResource(DEFAULT_AVATAR_DIR + DEFAULT_AVATAR);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + DEFAULT_AVATAR + "\"")
                    .contentLength(resource.contentLength())
                    .contentType(mediaType)
                    .body(new ByteArrayResource(resource.getInputStream().readAllBytes()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }
    }
}