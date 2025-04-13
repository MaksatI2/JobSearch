package org.example.JobSearch.controller;


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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String showProfilePage(Model model, Principal principal) {
        String email = principal.getName();
        UserDTO user = userService.getUserByEmail(email);

        String avatarUrl = "/api/users/" + user.getId() + "/avatar";
        user.setAvatar(avatarUrl);

        model.addAttribute("user", user);

        if (user.getAccountType() == AccountType.APPLICANT) {
            List<ResumeDTO> resumes = user.getId() == null ?
                    List.of() : resumeService.getResumesByApplicant(user.getId());
            int responsesCount = responseService.getResponsesCountByApplicant(email);
            model.addAttribute("resumes", resumes);
            model.addAttribute("responsesCount", responsesCount);
        } else if (user.getAccountType() == AccountType.EMPLOYER) {
            List<VacancyDTO> vacancies = user.getId() == null ?
                    List.of() : vacancyService.getVacanciesByEmployer(user.getId());
            model.addAttribute("vacancies", vacancies);
        }

        return "user/profile";
    }
}