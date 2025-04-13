package org.example.JobSearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.exceptions.UserNotFoundException;
import org.example.JobSearch.model.User;
import org.example.JobSearch.service.CategoryService;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.service.VacancyService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
}