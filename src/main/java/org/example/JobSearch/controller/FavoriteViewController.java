package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.service.FavoriteService;
import org.example.JobSearch.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteViewController {
    private final UserService userService;
    private final FavoriteService favoriteService;

    @GetMapping
    public String showFavoriteVacancies(Principal principal, Model model) {
        Long userId = userService.getUserId(principal.getName());
        List<VacancyDTO> favorites = favoriteService.getUserFavorites(userId);
        model.addAttribute("vacancies", favorites);
        return "vacancies/favorites";
    }

}
