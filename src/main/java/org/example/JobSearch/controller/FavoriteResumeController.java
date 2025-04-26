package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.service.FavoriteService;
import org.example.JobSearch.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/favorites/resumes")
@RequiredArgsConstructor
public class FavoriteResumeController {
    private final UserService userService;
    private final FavoriteService favoriteService;

    @GetMapping
    public String showFavoriteResumes(Principal principal, Model model) {
        Long userId = userService.getUserId(principal.getName());
        model.addAttribute("resumes", favoriteService.getUserFavoriteResumes(userId));
        return "resumes/favorites";
    }

    @PostMapping("/{id}/add")
    public String addFavorite(@PathVariable Long id, Principal principal) {
        Long userId = userService.getUserId(principal.getName());
        favoriteService.addResumeToFavorites(userId, id);
        return "redirect:/resumes";
    }

    @PostMapping("/{id}/remove")
    public String removeFavorite(@PathVariable Long id, Principal principal) {
        Long userId = userService.getUserId(principal.getName());
        favoriteService.removeResumeFromFavorites(userId, id);
        return "redirect:/favorites/resumes";
    }
}