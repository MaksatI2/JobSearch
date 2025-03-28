package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EditUserDTO;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.exceptions.UserNotFoundException;
import org.example.JobSearch.service.EditUserService;
import org.example.JobSearch.service.UserService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> updateUserByEmail(@PathVariable String email, @RequestBody EditUserDTO editUserDTO) {
        try {
            editUserService.updateUserByEmail(email, editUserDTO);
            return ResponseEntity.ok("User updated successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
            editUserService.deleteUserByEmail(email);
            return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<?> uploadUserAvatar(@PathVariable Long userId, @RequestParam MultipartFile file) {
            userService.updateUserAvatar(userId, file);
            return ResponseEntity.ok("Avatar updated successfully");
    }

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<?> getUserAvatar(@PathVariable Long userId) {
            return userService.getAvatarByUserId(userId);
    }

    @GetMapping("/findApplicant/email/{email}")
    public ResponseEntity<UserDTO> findApplicant(@PathVariable String email) {
        UserDTO user = userService.findApplicant(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/findApplicant/phone/{phoneNumber}")
    public ResponseEntity<UserDTO> findApplicantByPhone(@PathVariable String phoneNumber) {
        UserDTO user = userService.findApplicantByPhone(phoneNumber);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/findApplicant/name/{name}")
    public ResponseEntity<List<UserDTO>> findApplicantsByName(@PathVariable String name) {
        List<UserDTO> users = userService.findApplicantsByName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/findEmployer/email/{email}")
    public ResponseEntity<UserDTO> findEmployer(@PathVariable String email) {
        UserDTO user = userService.findEmployer(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/findEmployer/phone/{phoneNumber}")
    public ResponseEntity<UserDTO> findEmployerByPhone(@PathVariable String phoneNumber) {
        UserDTO user = userService.findEmployerByPhone(phoneNumber);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/findEmployer/name/{name}")
    public ResponseEntity<List<UserDTO>> findEmployersByName(@PathVariable String name) {
        List<UserDTO> users = userService.findEmployersByName(name);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable String email) {
        boolean exists = userService.userExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/vacancy/{vacancyId}/applicants")
    public ResponseEntity<?> getApplicantsForVacancy(@PathVariable("vacancyId") Long vacancyId) {
            List<UserDTO> applicants = userService.getApplicantsVacancy(vacancyId);
            return ResponseEntity.ok(applicants);
    }
}