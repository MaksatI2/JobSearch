package org.example.JobSearch.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.service.EditUserService;
import org.example.JobSearch.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EditUserService editUserService;
    private final MessageSource messageSource;

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<?> uploadUserAvatar(@PathVariable @Valid Long userId, @RequestParam MultipartFile file) {
            editUserService.updateUserAvatar(userId, file);
            return ResponseEntity.ok(getMessage("api.avatar"));
    }

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<?> getUserAvatar(@PathVariable @Valid Long userId) {
            return userService.getAvatarByUserId(userId);
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

}