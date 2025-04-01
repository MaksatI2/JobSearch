package org.example.JobSearch.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
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
    public static final String DEFAULT_APPLICANT_AVATAR = "default_avatar.jpg";
    public static final String DEFAULT_EMPLOYER_AVATAR = "default_company_logo.jpg";
    public static final String IMAGES_SUBDIR = "images/";

    static {
        initDefaultAvatars();
    }

    @SneakyThrows
    public static void initDefaultAvatars() {
        Path dir = Paths.get(UPLOAD_DIR + IMAGES_SUBDIR);
        Files.createDirectories(dir);

        Path applicantAvatarPath = dir.resolve(DEFAULT_APPLICANT_AVATAR);
        if (!Files.exists(applicantAvatarPath)) {
            log.warn("Аватар заявителя по умолчанию не найден по адресу: {}", applicantAvatarPath);
        }

        Path employerAvatarPath = dir.resolve(DEFAULT_EMPLOYER_AVATAR);
        if (!Files.exists(employerAvatarPath)) {
            log.warn("Аватар работодателя по умолчанию не найден по адресу: {}", employerAvatarPath);
        }
    }


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
    public static ResponseEntity<?> getOutputFile(String filename, String subDir, MediaType mediaType) {
        Path filePath = Paths.get(UPLOAD_DIR + subDir).resolve(filename);

        if (!Files.exists(filePath)) {
            Path applicantAvatarPath = Paths.get(UPLOAD_DIR + IMAGES_SUBDIR).resolve(DEFAULT_APPLICANT_AVATAR);
            if (Files.exists(applicantAvatarPath)) {
                filePath = applicantAvatarPath;
            } else {
                Path employerAvatarPath = Paths.get(UPLOAD_DIR + IMAGES_SUBDIR).resolve(DEFAULT_EMPLOYER_AVATAR);
                if (Files.exists(employerAvatarPath)) {
                    filePath = employerAvatarPath;
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Изображение не найдено");
                }
            }
        }

        byte[] fileBytes = Files.readAllBytes(filePath);
        Resource resource = new ByteArrayResource(fileBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "встроенный; имя файла=\"" + filename + "\"")
                .contentLength(resource.contentLength())
                .contentType(mediaType)
                .body(resource);
    }

}