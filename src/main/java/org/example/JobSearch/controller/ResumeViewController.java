package org.example.JobSearch.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.service.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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
        resumeService.validateEducation(resumeDTO.getEducationInfos(), bindingResult);

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
        editResumeDto.setEducationInfos(resume.getEducationInfos());
        editResumeDto.setWorkExperiences(resume.getWorkExperiences());

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
            @RequestParam(value = "addWorkExp", required = false) Boolean addWorkExp,
            @RequestParam(value = "addEducation", required = false) Boolean addEducation,
            @RequestParam(value = "removeWorkExp", required = false) Integer removeWorkExpIndex,
            @RequestParam(value = "removeEducation", required = false) Integer removeEducationIndex,
            Model model) {

        if (removeWorkExpIndex != null && editResumeDto.getWorkExperiences() != null
            && removeWorkExpIndex >= 0 && removeWorkExpIndex < editResumeDto.getWorkExperiences().size()) {
            editResumeDto.getWorkExperiences().remove(removeWorkExpIndex.intValue());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("resumeId", id);
            return "resumes/editResume";
        }

        if (removeEducationIndex != null && editResumeDto.getEducationInfos() != null
            && removeEducationIndex >= 0 && removeEducationIndex < editResumeDto.getEducationInfos().size()) {
            editResumeDto.getEducationInfos().remove(removeEducationIndex.intValue());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("resumeId", id);
            return "resumes/editResume";
        }

        if (addWorkExp != null && addWorkExp) {
            if (editResumeDto.getWorkExperiences() == null) {
                editResumeDto.setWorkExperiences(new ArrayList<>());
            }
            editResumeDto.getWorkExperiences().add(new WorkExperienceDTO());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("resumeId", id);
            return "resumes/editResume";
        }

        if (addEducation != null && addEducation) {
            if (editResumeDto.getEducationInfos() == null) {
                editResumeDto.setEducationInfos(new ArrayList<>());
            }
            EducationInfoDTO education = new EducationInfoDTO();
            education.setStartDate(new Date());
            education.setEndDate(new Date());
            editResumeDto.getEducationInfos().add(education);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("resumeId", id);
            return "resumes/editResume";
        }

        editResumeDto.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("resumeId", id);
            return "resumes/editResume";
        }

        resumeService.validateEducation(editResumeDto.getEducationInfos(), bindingResult);

        resumeService.updateResume(id, editResumeDto);
        return "redirect:/profile";
    }

    @GetMapping("/{id}/info")
    public String viewResume(@PathVariable Long id, HttpServletRequest request, Model model, Principal principal) {

        String currentUserEmail = principal.getName();
        String referer = request.getHeader("Referer");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        boolean isEmployer = authorities.stream().anyMatch(a -> a.getAuthority().equals("EMPLOYER"));

        ResumeDTO resume = resumeService.getResumeById(id);
        Long currentUserId = userService.getUserId(currentUserEmail);
        boolean isOwner = resume.getApplicantId().equals(currentUserId);

        if (!isOwner && !isEmployer) {
            return "errors/403";
        }

        model.addAttribute("resume", resume);
        model.addAttribute("isApplicant", isOwner);
        model.addAttribute("educationList", resume.getEducationInfos());
        model.addAttribute("workExperienceList", resume.getWorkExperiences());
        model.addAttribute("backUrl", referer != null ? referer : "/resumes/allResumes");

        return "resumes/resumeInfo";
    }


    @PostMapping("/{id}/refresh")
    public String refreshResume(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        UserDTO user = userService.getUserByEmail(email);

        ResumeDTO resume = resumeService.getResumeById(id);
        if (!resume.getApplicantId().equals(user.getId())) {
            throw new AccessDeniedException("Вы можете обновлять только свои собственные резюме.");
        }

        resumeService.refreshResume(id);
        return "redirect:/profile?refreshResumeSuccess";
    }

}