package org.example.JobSearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.model.Resume;
import org.example.JobSearch.service.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
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
        model.addAttribute("resumeForm", resumeForm);
        model.addAttribute("categories", categoryService.getAllCategories());

        resumeForm.setWorkExperiences(new ArrayList<>());
        resumeForm.setEducationInfos(new ArrayList<>());

        return "resumes/createResume";
    }

    @PostMapping("/create")
    public String handleResumeForm(
            @Valid @ModelAttribute("resumeForm") CreateResumeDTO resumeDTO,
            BindingResult bindingResult,
            @RequestParam(value = "addWorkExp", required = false) Boolean addWorkExp,
            @RequestParam(value = "addEducation", required = false) Boolean addEducation,
            @RequestParam(value = "removeWorkExp", required = false) Integer removeWorkExpIndex,
            @RequestParam(value = "removeEducation", required = false) Integer removeEducationIndex,
            Model model,
            Principal principal) {


        if (removeWorkExpIndex != null && resumeDTO.getWorkExperiences() != null
                && removeWorkExpIndex >= 0 && removeWorkExpIndex < resumeDTO.getWorkExperiences().size()) {
            resumeDTO.getWorkExperiences().remove(removeWorkExpIndex.intValue());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "resumes/createResume";
        }

        if (removeEducationIndex != null && resumeDTO.getEducationInfos() != null
                && removeEducationIndex >= 0 && removeEducationIndex < resumeDTO.getEducationInfos().size()) {
            resumeDTO.getEducationInfos().remove(removeEducationIndex.intValue());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "resumes/createResume";
        }

        if (addWorkExp != null && addWorkExp) {
            if (resumeDTO.getWorkExperiences() == null || resumeDTO.getWorkExperiences().isEmpty()) {
                resumeDTO.setWorkExperiences(new ArrayList<>());
                resumeDTO.getWorkExperiences().add(new WorkExperienceDTO());
            }
            model.addAttribute("categories", categoryService.getAllCategories());
            return "resumes/createResume";
        }

        if (addEducation != null && addEducation) {
            if (resumeDTO.getEducationInfos() == null || resumeDTO.getEducationInfos().isEmpty()) {
                resumeDTO.setEducationInfos(new ArrayList<>());
                EducationInfoDTO education = new EducationInfoDTO();
                education.setStartDate(new Date());
                education.setEndDate(new Date());
                resumeDTO.getEducationInfos().add(education);
            }
            model.addAttribute("categories", categoryService.getAllCategories());
            return "resumes/createResume";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "resumes/createResume";
        }

        resumeService.validateCreateResume(resumeDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "resumes/createResume";
        }

        String currentUserEmail = principal.getName();
        Long applicantId = userService.getUserId(currentUserEmail);
        resumeDTO.setApplicantId(applicantId);
        resumeDTO.setCreateDate(new Timestamp(System.currentTimeMillis()));
        resumeDTO.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        resumeService.createResume(resumeDTO, bindingResult);
        return "redirect:/profile";
    }

    @PostMapping(value = "/create", params = {"addWorkExp"})
    public String addWorkExperience(@ModelAttribute("resumeForm") CreateResumeDTO resumeDTO,
                                    Model model) {

        if (resumeDTO.getWorkExperiences() == null || resumeDTO.getWorkExperiences().isEmpty()) {
            resumeDTO.setWorkExperiences(new ArrayList<>());
        }
        resumeDTO.getWorkExperiences().add(new WorkExperienceDTO());

        model.addAttribute("resumeForm", resumeDTO);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "resumes/createResume";
    }


    @PostMapping("/addEducation")
    public String addEducation(@ModelAttribute("resumeForm") CreateResumeDTO resumeDTO,
                               Model model) {
        if (resumeDTO.getEducationInfos() == null || resumeDTO.getEducationInfos().isEmpty()) {
            resumeDTO.setEducationInfos(new ArrayList<>());
            EducationInfoDTO education = new EducationInfoDTO();
            education.setStartDate(new Date());
            education.setEndDate(new Date());
            resumeDTO.getEducationInfos().add(education);
        }

        model.addAttribute("resumeForm", resumeDTO);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "resumes/createResume";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        ResumeDTO resume = resumeService.getResumeById(id);

        String currentUserEmail = principal.getName();
        Long currentUserId = userService.getUserId(currentUserEmail);

        if (!resume.getApplicantId().equals(currentUserId)) {
            throw new AccessDeniedException("Вы не можете редактировать это резюме");
        }

        EditResumeDTO editResumeDto = new EditResumeDTO();
        editResumeDto.setId(resume.getId());
        editResumeDto.setName(resume.getName());
        editResumeDto.setCategoryId(resume.getCategoryId());
        editResumeDto.setSalary(resume.getSalary());
        editResumeDto.setIsActive(resume.getIsActive());

        model.addAttribute("resumeForm", editResumeDto);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("resumeId", id);
        return "resumes/editResume";
    }

    @PostMapping("/{id}/edit")
    public String handleEditForm(
            @PathVariable Long id,
            @Valid @ModelAttribute("resumeForm") EditResumeDTO editResumeDto,
            BindingResult bindingResult,
            Model model) {

        editResumeDto.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("resumeId", id);
            return "resumes/editResume";
        }

        resumeService.updateResume(id, editResumeDto);
        return "redirect:/profile";
    }
}