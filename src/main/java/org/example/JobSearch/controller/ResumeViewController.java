package org.example.JobSearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.exceptions.CreateResumeException;
import org.example.JobSearch.service.CategoryService;
import org.example.JobSearch.service.ResumeService;
import org.example.JobSearch.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

@Controller
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeViewController {
    private final ResumeService resumeService;
    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping("/allResumes")
    public String getAllActiveResumes(Model model) {
            model.addAttribute("resumes", resumeService.getAllResumes());
        return "resumes/allResumes";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        CreateResumeDTO resumeForm = new CreateResumeDTO();

        EducationInfoDTO defaultEducation = new EducationInfoDTO();
        defaultEducation.setStartDate(new Date());
        defaultEducation.setEndDate(new Date());

        model.addAttribute("resumeForm", resumeForm);
        model.addAttribute("categories", categoryService.getAllCategories());

        model.addAttribute("workExperiences", Collections.singletonList(new WorkExperienceDTO()));
        model.addAttribute("educationInfos", Collections.singletonList(defaultEducation));

        return "resumes/createResume";
    }

    @PostMapping("/create")
    public String createResume(@Valid @ModelAttribute("resumeForm") CreateResumeDTO resumeDTO,
                               BindingResult bindingResult,
                               Model model,
                               Principal principal) {

        try {
            resumeService.validateCreateResume(resumeDTO);
        } catch (CreateResumeException e) {
            bindingResult.rejectValue(e.getFieldName(), "error.resumeForm", e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("workExperiences", resumeDTO.getWorkExperiences() != null ?
                    resumeDTO.getWorkExperiences() : Collections.singletonList(new WorkExperienceDTO()));
            model.addAttribute("educationInfos", resumeDTO.getEducationInfos() != null ?
                    resumeDTO.getEducationInfos() : Collections.singletonList(new EducationInfoDTO()));
            return "resumes/createResume";
        }

        String currentUserEmail = principal.getName();
        Long applicantId = userService.getUserId(currentUserEmail);
        resumeDTO.setApplicantId(applicantId);
        resumeDTO.setCreateDate(new Timestamp(System.currentTimeMillis()));
        resumeDTO.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        resumeService.createResume(resumeDTO);

        return "redirect:/profile";
    }

    @PostMapping(value = "/create", params = {"addWorkExp"})
    public String addWorkExperience(@ModelAttribute("resumeForm") CreateResumeDTO resumeDTO,
                                    BindingResult bindingResult,
                                    Model model) {
        if (resumeDTO.getWorkExperiences() == null) {
            resumeDTO.setWorkExperiences(new ArrayList<>());
        }
        resumeDTO.getWorkExperiences().add(new WorkExperienceDTO());

        try {
            resumeService.validateCreateResume(resumeDTO);
        } catch (CreateResumeException e) {
            bindingResult.rejectValue(e.getFieldName(), "error.resumeForm", e.getMessage());
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("workExperiences", resumeDTO.getWorkExperiences());
        model.addAttribute("educationInfos", resumeDTO.getEducationInfos() != null ?
                resumeDTO.getEducationInfos() : Collections.singletonList(new EducationInfoDTO()));

        return "resumes/createResume";
    }

    @PostMapping(value = "/create", params = {"addEducation"})
    public String addEducation(@ModelAttribute("resumeForm") CreateResumeDTO resumeDTO,
                               BindingResult bindingResult,
                               Model model) {
        if (resumeDTO.getEducationInfos() == null) {
            resumeDTO.setEducationInfos(new ArrayList<>());
        }

        EducationInfoDTO newEducation = new EducationInfoDTO();
        newEducation.setStartDate(new Date());
        newEducation.setEndDate(new Date());

        resumeDTO.getEducationInfos().add(newEducation);

        try {
            resumeService.validateCreateResume(resumeDTO);
        } catch (CreateResumeException e) {
            bindingResult.rejectValue(e.getFieldName(), "error.resumeForm", e.getMessage());
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("workExperiences", resumeDTO.getWorkExperiences() != null ?
                resumeDTO.getWorkExperiences() : Collections.singletonList(new WorkExperienceDTO()));
        model.addAttribute("educationInfos", resumeDTO.getEducationInfos());

        return "resumes/createResume";
    }
}