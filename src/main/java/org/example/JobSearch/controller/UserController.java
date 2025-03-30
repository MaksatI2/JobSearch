package org.example.JobSearch.controller;

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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EditUserService editUserService;

    @PutMapping("/{email}")
    public ResponseEntity<?> updateUserByEmail(@PathVariable @Valid String email, @RequestBody EditUserDTO editUserDTO) {
            editUserService.updateUserByEmail(email, editUserDTO);
            return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable @Valid String email) {
            editUserService.deleteUserByEmail(email);
            return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<?> uploadUserAvatar(@PathVariable @Valid Long userId, @RequestParam MultipartFile file) {
            userService.updateUserAvatar(userId, file);
            return ResponseEntity.ok("Avatar updated successfully");
    }

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<?> getUserAvatar(@PathVariable @Valid Long userId) {
            return userService.getAvatarByUserId(userId);
    }

    @GetMapping("/applicants/email/{email}")
    public ResponseEntity<UserDTO> findApplicant(@PathVariable @Valid String email) {
        UserDTO user = userService.findApplicant(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/applicants/phone/{phoneNumber}")
    public ResponseEntity<UserDTO> findApplicantByPhone(@PathVariable @Valid String phoneNumber) {
        UserDTO user = userService.findApplicantByPhone(phoneNumber);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/applicants/name/{name}")
    public ResponseEntity<List<UserDTO>> findApplicantsByName(@PathVariable @Valid String name) {
        List<UserDTO> users = userService.findApplicantsByName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/employers/email/{email}")
    public ResponseEntity<UserDTO> findEmployer(@PathVariable @Valid String email) {
        UserDTO user = userService.findEmployer(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/employers/phone/{phoneNumber}")
    public ResponseEntity<UserDTO> findEmployerByPhone(@PathVariable @Valid String phoneNumber) {
        UserDTO user = userService.findEmployerByPhone(phoneNumber);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/employers/name/{name}")
    public ResponseEntity<List<UserDTO>> findEmployersByName(@PathVariable @Valid String name) {
        List<UserDTO> users = userService.findEmployersByName(name);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable @Valid String email) {
        boolean exists = userService.userExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/vacancies/{vacancyId}/applicants")
    public ResponseEntity<?> getApplicantsForVacancy(@PathVariable("vacancyId") @Valid Long vacancyId) {
            List<UserDTO> applicants = userService.getApplicantsVacancy(vacancyId);
            return ResponseEntity.ok(applicants);
    }
}