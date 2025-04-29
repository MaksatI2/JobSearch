package org.example.JobSearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.mapper.ResumeMapper;
import org.example.JobSearch.dto.*;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.model.RespondedApplicant;
import org.example.JobSearch.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeViewController {
    private final ResumeService resumeService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FavoriteResumeService favoriteService;
    private final ResponseService responseService;

    @GetMapping("/allResumes")
    public String getAllActiveResumes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort,
            Model model,
            Authentication authentication) {

        Page<ResumeDTO> resumesPage = resumeService.getAllResumes(sort, PageRequest.of(page, size));

        resumesPage.getContent().forEach(resume -> {
            String avatarUrl = "/api/users/" + resume.getApplicantId() + "/avatar";
            resume.setApplicantAvatar(avatarUrl);
        });

        String email = authentication.getName();
        UserDTO user = userService.getUserByEmail(email);

        List<Long> favoriteResumeIds = favoriteService.getFavoriteResumeIds(user.getId());
        model.addAttribute("favoriteResumeIds", favoriteResumeIds);

        model.addAttribute("resumes", resumesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resumesPage.getTotalPages());
        model.addAttribute("totalItems", resumesPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedSort", sort);

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

        List<ContactInfoDTO> existingContacts = resume.getContactInfos() != null ? resume.getContactInfos() : new ArrayList<>();
        Map<Long, ContactInfoDTO> contactMap = existingContacts.stream()
                .collect(Collectors.toMap(ContactInfoDTO::getTypeId, c -> c));

        List<ContactInfoDTO> contactInfos = new ArrayList<>();
        for (long i = 1; i <= 6; i++) {
            if (contactMap.containsKey(i)) {
                contactInfos.add(contactMap.get(i));
            } else {
                ContactInfoDTO emptyContact = new ContactInfoDTO();
                emptyContact.setTypeId(i);
                contactInfos.add(emptyContact);
            }
        }
        editResumeDto.setContactInfos(contactInfos);

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
    public String viewResume(@PathVariable Long id, Model model, Principal principal) {
        ResumeDTO resume = resumeService.getResumeById(id);
        Long currentUserId = userService.getUserId(principal.getName());

        boolean isOwner = resume.getApplicantId().equals(currentUserId);
        boolean isEmployer = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("EMPLOYER"));

        if (!isOwner && !isEmployer) {
            throw new AccessDeniedException("У вас нет доступа к этому резюме");
        }

        resume.setApplicantAvatar("/api/users/" + resume.getApplicantId() + "/avatar");

        model.addAttribute("resume", resume);
        model.addAttribute("isApplicant", isOwner);
        model.addAttribute("educationList", resume.getEducationInfos());
        model.addAttribute("workExperienceList", resume.getWorkExperiences());

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

    @PostMapping("/{id}/delete")
    public String deleteResume(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        UserDTO user = userService.getUserByEmail(email);

        ResumeDTO resume = resumeService.getResumeById(id);
        if (!resume.getApplicantId().equals(user.getId())) {
            throw new AccessDeniedException("Вы можете удалять только свои собственные резюме.");
        }

        resumeService.deleteResume(id);
        return "redirect:/profile?deleteSuccess";
    }

    @GetMapping("/{userId}/response")
    public String showResumesWithResponses(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model,
            Principal principal) {
        if (principal != null) {
            String currentUserEmail = principal.getName();
            Long currentUserId = userService.getUserId(currentUserEmail);

            if (!currentUserId.equals(userId)) {
                throw new AccessDeniedException("Вы можете просматривать только свои резюме с откликами");
            }

            Page<ResumeDTO> resumesPage = resumeService.getResumesWithResponsesByApplicantId(
                    userId,
                    PageRequest.of(page, size)
            );

            responseService.markApplicantResponsesAsViewed(userId);

            model.addAttribute("resumes", resumesPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", resumesPage.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("userId", userId);

            return "resumes/resumesResponse";
        } else {
            return "redirect:/auth/login";
        }
    }

}