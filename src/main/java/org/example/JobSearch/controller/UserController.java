package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/findApplicant/{userEmail}")
    public ResponseEntity<String> findApplicant(@PathVariable("userEmail") Long userEmail) {
        userService.findApplicant(userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body("Applicant successfully found");
    }

    @GetMapping("/findEmployer/{userEmail}")
    public ResponseEntity<String> findEmployer(@PathVariable("userEmail") Long userEmail) {
        userService.findEmployer(userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employer successfully found");
    }

    @GetMapping("/responded/{vacancyId}")
    public ResponseEntity<String> getApplicantsForVacancy(@PathVariable("vacancyId") Long vacancyId){
        userService.getApplicantsVacancy(vacancyId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Applicants successfully found");
    }
}
