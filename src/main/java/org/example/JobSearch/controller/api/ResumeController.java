package org.example.JobSearch.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

//    @PostMapping
//    public ResponseEntity<String> createResume(@RequestBody ResumeDTO resumeDTO) {
//        resumeService.createResume(resumeDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Resume created successfully");
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateResume(@PathVariable @Valid Long id, @RequestBody EditResumeDTO editresumeDTO) {
//        resumeService.updateResume(id, editresumeDTO);
//        return ResponseEntity.ok("Resume updated successfully");
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResume(@PathVariable @Valid Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.ok("Resume deleted successfully");
    }

//    @GetMapping("/allResumes")
//    public ResponseEntity<?> getAllResumes() {
//            List<ResumeDTO> resumes = resumeService.getAllResumes();
//            return ResponseEntity.ok(resumes);
//    }

    @GetMapping("/UserResumes/{applicantId}")
    public ResponseEntity<?> getUserResumes(@PathVariable @Valid Long applicantId) {
            List<ResumeDTO> resumes = resumeService.getUserResumes(applicantId);
            return ResponseEntity.ok(resumes);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getResumesByCategory(@PathVariable @Valid Long categoryId) {
            List<ResumeDTO> resumes = resumeService.getResumesByCategory(categoryId);
            return ResponseEntity.ok(resumes);
    }
}