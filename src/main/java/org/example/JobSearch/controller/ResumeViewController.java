package org.example.JobSearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EditDTO.EditEducationInfoDTO;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.EditDTO.EditWorkExperienceDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.exceptions.CreateResumeException;
import org.example.JobSearch.service.CategoryService;
import org.example.JobSearch.service.ResumeService;
import org.example.JobSearch.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        ResumeDTO resume = resumeService.getResumeById(id);

        String currentUserEmail = principal.getName();
        Long currentUserId = userService.getUserId(currentUserEmail);

        if (!resume.getApplicantId().equals(currentUserId)) {
            throw new AccessDeniedException("Вы не можете редактировать это резюме");
        }

        EditResumeDTO form = resumeService.convertToEditDTO(resume);
        if (form.getEducationInfos() == null || form.getEducationInfos().isEmpty()) {
            form.setEducationInfos(Collections.singletonList(new EditEducationInfoDTO()));
        }
        if (form.getWorkExperiences() == null || form.getWorkExperiences().isEmpty()) {
            form.setWorkExperiences(Collections.singletonList(new EditWorkExperienceDTO()));
        }

        model.addAttribute("resumeForm", form);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("resumeId", id);
        return "resumes/editResume";
    }

    @PostMapping("/{id}/edit")
    public String editResume(@PathVariable Long id,
                             @Valid @ModelAttribute("resumeForm") EditResumeDTO form,
                             BindingResult bindingResult,
                             Model model, Principal principal) {

        String currentUserEmail = principal.getName();
        Long currentUserId = userService.getUserId(currentUserEmail);

        ResumeDTO resume = resumeService.getResumeById(id);
        if (!resume.getApplicantId().equals(currentUserId)) {
            throw new AccessDeniedException("Вы не можете редактировать это резюме");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("resumeId", id);
            return "resumes/editResume";
        }

        form.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        resumeService.updateResume(id, form);

        return "redirect:/profile";
    }

    @PostMapping(value = "/{id}/edit", params = {"addWorkExp"})
    public String addWorkExperience(@PathVariable Long id,
                                    @ModelAttribute("resumeForm") EditResumeDTO form,
                                    BindingResult bindingResult,
                                    Model model, Principal principal) {
        if (form.getWorkExperiences() == null) {
            form.setWorkExperiences(new ArrayList<>());
        }
        form.getWorkExperiences().add(new EditWorkExperienceDTO());

        prepareModelForEditForm(id, form, model, principal);
        return "resumes/editResume";
    }

    @PostMapping(value = "/{id}/edit", params = {"removeWorkExp"})
    public String removeWorkExperience(@PathVariable Long id,
                                       @ModelAttribute("resumeForm") EditResumeDTO form,
                                       @RequestParam("removeWorkExp") int index,
                                       BindingResult bindingResult,
                                       Model model, Principal principal) {
        if (form.getWorkExperiences() != null && index >= 0 && index < form.getWorkExperiences().size()) {
            form.getWorkExperiences().remove(index);
        }

        prepareModelForEditForm(id, form, model, principal);
        return "resumes/editResume";
    }

    @PostMapping(value = "/{id}/edit", params = {"addEducation"})
    public String addEducation(@PathVariable Long id,
                               @ModelAttribute("resumeForm") EditResumeDTO form,
                               BindingResult bindingResult,
                               Model model, Principal principal) {
        if (form.getEducationInfos() == null) {
            form.setEducationInfos(new ArrayList<>());
        }
        form.getEducationInfos().add(new EditEducationInfoDTO());

        prepareModelForEditForm(id, form, model, principal);
        return "resumes/editResume";
    }

    @PostMapping(value = "/{id}/edit", params = {"removeEducation"})
    public String removeEducation(@PathVariable Long id,
                                  @ModelAttribute("resumeForm") EditResumeDTO form,
                                  @RequestParam("removeEducation") int index,
                                  BindingResult bindingResult,
                                  Model model, Principal principal) {
        if (form.getEducationInfos() != null && index >= 0 && index < form.getEducationInfos().size()) {
            form.getEducationInfos().remove(index);
        }

        prepareModelForEditForm(id, form, model, principal);
        return "resumes/editResume";
    }

    private void prepareModelForEditForm(Long id, EditResumeDTO form, Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        Long currentUserId = userService.getUserId(currentUserEmail);

        ResumeDTO resume = resumeService.getResumeById(id);
        if (!resume.getApplicantId().equals(currentUserId)) {
            throw new AccessDeniedException("Вы не можете редактировать это резюме");
        }

        model.addAttribute("resumeForm", form);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("resumeId", id);
    }
}