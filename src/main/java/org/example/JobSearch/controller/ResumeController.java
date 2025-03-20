package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<String> createResume(@RequestBody ResumeDTO resumeDTO) {
        resumeService.createResume(resumeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Resume created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateResume(@PathVariable Long id, @RequestBody ResumeDTO resumeDTO) {
        resumeService.updateResume(id, resumeDTO);
        return ResponseEntity.ok("Resume updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.ok("Resume deleted successfully");
    }

    @GetMapping("/allResumes")
    public List<ResumeDTO> getAllResumes() {
        return resumeService.getAllResumes();
    }

    @GetMapping("/getUserResumes/{applicants_id}")
    public List<ResumeDTO> getUserResumes(@PathVariable Long applicants_id) {
        return resumeService.getUserResumes(applicants_id);
    }

    @GetMapping("/resumes/category/{categoryId}")
    public List<ResumeDTO> getResumesByCategory(@PathVariable Long categoryId) {
        return resumeService.getResumesByCategory(categoryId);
    }
}