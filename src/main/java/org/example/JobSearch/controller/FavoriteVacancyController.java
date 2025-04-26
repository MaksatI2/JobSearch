package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.service.FavoriteService;
import org.example.JobSearch.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/favorites/vacancies")
@RequiredArgsConstructor
public class FavoriteVacancyController {
    private final UserService userService;
    private final FavoriteService favoriteService;

    @GetMapping
    public String showFavoriteVacancies(Principal principal, Model model) {
        Long userId = userService.getUserId(principal.getName());
        List<VacancyDTO> favorites = favoriteService.getUserFavoriteVacancies(userId);
        model.addAttribute("vacancies", favorites);
        return "vacancies/favorites";
    }

    @PostMapping("/{id}/add")
    public String addFavorite(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Long userId = userService.getUserId(principal.getName());
        favoriteService.addVacancyToFavorites(userId, id);
        redirectAttributes.addFlashAttribute("successMessage", "Вакансия добавлена в избранное.");
        return "redirect:/vacancies";
    }

    @PostMapping("/{id}/remove")
    public String removeFavorite(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Long userId = userService.getUserId(principal.getName());
        favoriteService.removeVacancyFromFavorites(userId, id);
        redirectAttributes.addFlashAttribute("successMessage", "Вакансия удалена из избранного.");
        return "redirect:/favorites/vacancies";
    }
}