package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.UserDTO;
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

    @PostMapping("/uploadAvatar")
    public String uploadAvatar(MultipartFile file) {
        return userService.addAvatar(file);
    }

    @GetMapping("avatar/{imageName}")
    public ResponseEntity<?> getImage(@PathVariable String imageName) {
        return userService.getAvatarByName(imageName);
    }

    @GetMapping("/findApplicant/email/{email}")
    public ResponseEntity<UserDTO> findApplicant(@PathVariable String email) {
        return ResponseEntity.ok(userService.findApplicant(email));
    }

    @GetMapping("/findApplicant/phone/{phoneNumber}")
    public ResponseEntity<UserDTO> findApplicantByPhone(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(userService.findApplicantByPhone(phoneNumber));
    }

    @GetMapping("/findApplicant/name/{name}")
    public ResponseEntity<List<UserDTO>> findApplicantsByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.findApplicantsByName(name));
    }

    @GetMapping("/findEmployer/email/{email}")
    public ResponseEntity<UserDTO> findEmployer(@PathVariable String email) {
        return ResponseEntity.ok(userService.findEmployer(email));
    }

    @GetMapping("/findEmployer/phone/{phoneNumber}")
    public ResponseEntity<UserDTO> findEmployerByPhone(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(userService.findEmployerByPhone(phoneNumber));
    }

    @GetMapping("/findEmployer/name/{name}")
    public ResponseEntity<List<UserDTO>> findEmployersByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.findEmployersByName(name));
    }

    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable String email) {
        boolean exists = userService.userExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/responded/{vacancyId}")
    public ResponseEntity<String> getApplicantsForVacancy(@PathVariable("vacancyId") Long vacancyId){
        userService.getApplicantsVacancy(vacancyId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Applicants successfully found");
    }
}
