package org.example.JobSearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.service.CategoryService;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.service.VacancyService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacancyViewController {
    private final VacancyService vacancyService;
    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping()
    public String showVacancies(Model model) {
        model.addAttribute("vacancies", vacancyService.getAllVacancies());
        return "vacancies/vacancies";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("vacancyForm", new CreateVacancyDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "vacancies/createVacancy";
    }

    @PostMapping("/create")
    public String addVacancy(CreateVacancyDTO vacancyDTO) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentUser = userService.getUserId(currentUserEmail);
        vacancyService.createVacancy(vacancyDTO, currentUser);

        return "redirect:/profile";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        VacancyDTO vacancy = vacancyService.getVacancyById(id);

        String currentUserEmail = principal.getName();
        Long currentUserId = userService.getUserId(currentUserEmail);

        if (!vacancy.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException("Вы не можете редактировать эту вакансию");
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
                              Model model, Principal principal) {

        String currentUserEmail = principal.getName();
        Long currentUserId = userService.getUserId(currentUserEmail);

        VacancyDTO vacancy = vacancyService.getVacancyById(id);
        if (!vacancy.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException("Вы не можете редактировать эту вакансию");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("vacancyId", id);
            return "vacancies/editVacancy";
        }

        form.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        vacancyService.updateVacancy(id, form);

        return "redirect:/profile";
    }

}