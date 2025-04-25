package org.example.JobSearch.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EditDTO.EditUserDTO;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.service.EditUserService;
import org.example.JobSearch.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EditUserService editUserService;

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<?> uploadUserAvatar(@PathVariable @Valid Long userId, @RequestParam MultipartFile file) {
            editUserService.updateUserAvatar(userId, file);
            return ResponseEntity.ok("Avatar updated successfully");
    }

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<?> getUserAvatar(@PathVariable @Valid Long userId) {
            return userService.getAvatarByUserId(userId);
    }

}