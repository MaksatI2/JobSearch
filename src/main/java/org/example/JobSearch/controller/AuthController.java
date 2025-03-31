package org.example.JobSearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}