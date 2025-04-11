package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.service.ResumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeViewController {

    private final ResumeService resumeService;

    @GetMapping("/allResumes")
    public String getAllActiveResumes(Model model) {
            model.addAttribute("resumes", resumeService.getAllResumes());
        return "resumes/allResumes";
    }
}