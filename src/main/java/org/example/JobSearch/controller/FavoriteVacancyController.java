package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.service.FavoriteVacancyService;
import org.example.JobSearch.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/favorites/vacancies")
@RequiredArgsConstructor
public class FavoriteVacancyController {
    private final UserService userService;
    private final FavoriteVacancyService favoriteService;
    private final MessageSource messageSource;

    @GetMapping
    public String showFavoriteVacancies(
            Principal principal,
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        UserDTO user = userService.getUserByEmail(principal.getName());

        if (user.getAccountType() != AccountType.APPLICANT) {
            throw new AccessDeniedException(getMessage("validate.forbidden"));
        }
        Page<VacancyDTO> vacanciesPage = favoriteService.getUserFavoriteVacancies(
                user.getId(),
                PageRequest.of(page, size)
        );

        model.addAttribute("vacanciesPage", vacanciesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", vacanciesPage.getTotalPages());

        return "vacancies/favorites";
    }

    @PostMapping("/{id}/add")
    public String addFavorite(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Long userId = userService.getUserId(principal.getName());
        favoriteService.addVacancyToFavorites(userId, id);
        redirectAttributes.addFlashAttribute("successMessage", getMessage("vacancy.save"));
        return "redirect:/vacancies";
    }

    @PostMapping("/{id}/remove")
    public String removeFavorite(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Long userId = userService.getUserId(principal.getName());
        favoriteService.removeVacancyFromFavorites(userId, id);
        redirectAttributes.addFlashAttribute("successMessage", getMessage("vacancy.delete"));
        return "redirect:/favorites/vacancies";
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}