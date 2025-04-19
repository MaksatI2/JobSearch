package org.example.JobSearch.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.service.ResponseService;
import org.example.JobSearch.service.ResumeService;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.service.VacancyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final ResponseService responseService;

    @GetMapping
    public String showProfilePage(
            HttpServletRequest request,
            Model model,
            Principal principal,
            @RequestParam(name = "resumePage", defaultValue = "1") int resumePage,
            @RequestParam(name = "vacancyPage", defaultValue = "1") int vacancyPage) {

        String email = principal.getName();
        String referer = request.getHeader("Referer");
        UserDTO user = userService.getUserByEmail(email);

        String avatarUrl = "/api/users/" + user.getId() + "/avatar";
        user.setAvatar(avatarUrl);

        model.addAttribute("user", user);

        if (user.getAccountType() == AccountType.APPLICANT) {
            int pageSize = 3;
            Page<ResumeDTO> resumesPage = resumeService.getResumesByApplicant(user.getId(), resumePage - 1, pageSize);
            model.addAttribute("resumes", resumesPage.getContent());
            model.addAttribute("currentResumePage", resumePage);
            model.addAttribute("totalResumePages", resumesPage.getTotalPages());

            int responsesCount = responseService.getResponsesCountByApplicant(email);
            model.addAttribute("backUrl", referer != null ? referer : "/profile");
            model.addAttribute("responsesCount", responsesCount);
        } else if (user.getAccountType() == AccountType.EMPLOYER) {
            int pageSize = 3;
            Page<VacancyDTO> vacanciesPage = vacancyService.getVacanciesByEmployer(user.getId(), vacancyPage - 1, pageSize);
            model.addAttribute("vacancies", vacanciesPage.getContent());
            model.addAttribute("currentVacancyPage", vacancyPage);
            model.addAttribute("totalVacancyPages", vacanciesPage.getTotalPages());

            model.addAttribute("backUrl", referer != null ? referer : "/profile");
        }

        return "user/profile";
    }
}