package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.service.FavoriteResumeService;
import org.example.JobSearch.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/favorites/resumes")
@RequiredArgsConstructor
public class FavoriteResumeController {
    private final UserService userService;
    private final FavoriteResumeService favoriteService;
    private final MessageSource messageSource;

    @GetMapping
    public String showFavoriteResumes(Principal principal, Model model) {
        Long userId = userService.getUserId(principal.getName());
        model.addAttribute("resumes", favoriteService.getUserFavoriteResumes(userId));
        return "resumes/favorites";
    }

    @PostMapping("/{id}/add")
    public String addFavorite(@PathVariable Long id, Principal principal,  RedirectAttributes redirectAttributes) {
        Long userId = userService.getUserId(principal.getName());
        favoriteService.addResumeToFavorites(userId, id);
        redirectAttributes.addFlashAttribute("successMessage", getMessage("resume.save"));
        return "redirect:/resumes/allResumes";
    }

    @PostMapping("/{id}/remove")
    public String removeFavorite(@PathVariable Long id, Principal principal,  RedirectAttributes redirectAttributes) {
        Long userId = userService.getUserId(principal.getName());
        favoriteService.removeResumeFromFavorites(userId, id);
        redirectAttributes.addFlashAttribute("successMessage", getMessage("resume.delete"));
        return "redirect:/favorites/resumes";
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}