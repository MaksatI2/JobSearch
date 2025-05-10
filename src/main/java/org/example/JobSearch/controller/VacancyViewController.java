package org.example.JobSearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.exceptions.CreateVacancyException;
import org.example.JobSearch.exceptions.EditVacancyException;
import org.example.JobSearch.service.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacancyViewController {
    private final VacancyService vacancyService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ResumeService resumeService;
    private final FavoriteVacancyService favoriteService;
    private final ResponseService responseService;
    private final MessageSource messageSource;


    @GetMapping
    public String showVacancies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort,
            Model model,
            Authentication authentication) {

        Page<VacancyDTO> vacanciesPage = vacancyService.getAllVacanciesSorted(
                sort,
                PageRequest.of(page, size)
        );

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            UserDTO user = userService.getUserByEmail(email);

            List<Long> favoriteVacancyIds = favoriteService.getFavoriteVacancyIds(user.getId());
            model.addAttribute("favoriteVacancyIds", favoriteVacancyIds);

            Page<ResumeDTO> resumesPage = resumeService.getResumesByApplicant(user.getId(), 0, 10);
            model.addAttribute("userResumes", resumesPage.getContent());

        }

        model.addAttribute("vacancies", vacanciesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vacanciesPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedSort", sort);

        return "vacancies/vacancies";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("vacancyForm", new CreateVacancyDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "vacancies/createVacancy";
    }

    @PostMapping("/create")
    public String addVacancy(@Valid @ModelAttribute("vacancyForm") CreateVacancyDTO vacancyDTO,
                             BindingResult bindingResult,
                             Model model,
                             Principal principal) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "vacancies/createVacancy";
        }

        try {
            String currentUserEmail = principal.getName();
            Long currentUser = userService.getUserId(currentUserEmail);

            vacancyService.validateVacancyData(vacancyDTO, bindingResult);

            vacancyService.createVacancy(vacancyDTO, currentUser);

            return "redirect:/profile";

        } catch (CreateVacancyException e) {
            log.error("Create vacancy error: {}", e.getMessage());
            bindingResult.rejectValue(e.getFieldName(), "error.createVacancyDTO", e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "vacancies/createVacancy";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        VacancyDTO vacancy = vacancyService.getVacancyById(id);

        String currentUserEmail = principal.getName();
        Long currentUserId = userService.getUserId(currentUserEmail);

        if (!vacancy.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException(getMessage("vacancy.edit.forbidden"));
        }

        EditVacancyDTO form = vacancyService.convertToEditDTO(vacancy);
        model.addAttribute("vacancyForm", form);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("vacancyId", id);
        return "vacancies/editVacancy";
    }

    @PostMapping("/{id}/edit")
    public String editVacancy(@PathVariable Long id,
                              @Valid @ModelAttribute("vacancyForm") EditVacancyDTO form,
                              BindingResult bindingResult,
                              Model model) {

        try {
            vacancyService.validateEditVacancyData(form, bindingResult);

            if (bindingResult.hasErrors()) {
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("vacancyId", id);
                return "vacancies/editVacancy";
            }

            form.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
            vacancyService.updateVacancy(id, form);

            return "redirect:/profile";

        } catch (EditVacancyException e) {
            log.error("Edit vacancy error: {}", e.getMessage());
            bindingResult.rejectValue(e.getFieldName(), "error.editVacancyDTO", e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("vacancyId", id);
            return "vacancies/editVacancy";
        }
    }

    @GetMapping("/{id}/info")
    public String showVacancyDetails(@PathVariable Long id,
                                     Model model,
                                     Principal principal,
                                     Authentication authentication) {
        VacancyDTO vacancy = vacancyService.getVacancyById(id);
        model.addAttribute("vacancy", vacancy);

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            UserDTO user = userService.getUserByEmail(email);

            Page<ResumeDTO> resumesPage = resumeService.getResumesByApplicant(user.getId(), 0, 10);
            model.addAttribute("resumesByUser", resumesPage.getContent());
        }

        if (principal != null) {
            String currentUserEmail = principal.getName();
            Long currentUserId = userService.getUserId(currentUserEmail);
            model.addAttribute("isAuthor", vacancy.getAuthorId().equals(currentUserId));
        } else {
            model.addAttribute("isAuthor", false);
        }

        return "vacancies/vacancyInfo";
    }

    @PostMapping("/{id}/refresh")
    public String refreshVacancy(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        UserDTO user = userService.getUserByEmail(email);

        VacancyDTO vacancy = vacancyService.getVacancyById(id);
        if (!vacancy.getAuthorId().equals(user.getId())) {
            throw new AccessDeniedException(getMessage("vacancy.refresh.forbidden"));
        }

        vacancyService.refreshVacancy(id);
        return "redirect:/profile?refreshSuccess";
    }

    @PostMapping("/{id}/delete")
    public String deleteVacancy(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        UserDTO user = userService.getUserByEmail(email);

        VacancyDTO vacancy = vacancyService.getVacancyById(id);
        if (!vacancy.getAuthorId().equals(user.getId())) {
            throw new AccessDeniedException(getMessage("vacancy.delete.forbidden"));
        }

        vacancyService.deleteVacancy(id);
        return "redirect:/profile?deleteSuccess";
    }

    @GetMapping("/{userId}/response")
    public String showVacanciesWithResponses(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model,
            Principal principal) {
        if (principal != null) {
            String currentUserEmail = principal.getName();
            Long currentUserId = userService.getUserId(currentUserEmail);

            if (!currentUserId.equals(userId)) {
                throw new AccessDeniedException(getMessage("vacancy.view.forbidden"));
            }

            Page<VacancyDTO> vacanciesPage = vacancyService.getVacanciesWithResponsesByAuthorId(
                    userId,
                    PageRequest.of(page, size)
            );

            responseService.markEmployerResponsesAsViewed(userId);

            model.addAttribute("vacancies", vacanciesPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", vacanciesPage.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("userId", userId);

            return "vacancies/vacanciesResponses";
        } else {
            return "redirect:/auth/login";
        }
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

}