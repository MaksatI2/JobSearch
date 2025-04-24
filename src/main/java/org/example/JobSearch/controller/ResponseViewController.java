package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.service.ResponseService;
import org.example.JobSearch.service.ResumeService;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.service.VacancyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/response")
@RequiredArgsConstructor
public class ResponseViewController {

    private final ResponseService responseService;
    private final ResumeService resumeService;
    private final UserService userService;
    private final VacancyService vacancyService;

    @GetMapping("/vacancies/{id}")
    public String responseByVacancies(@PathVariable Long id,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      Model model,
                                      Principal principal) {

        VacancyDTO vacancy = vacancyService.getVacancyById(id);
        String email = principal.getName();
        UserDTO user = userService.getUserByEmail(email);

        if (!vacancy.getAuthorId().equals(user.getId())) {
            throw new AccessDeniedException("У вас нет доступа к этой вакансии");
        }

        Page<ResumeDTO> resumesPage = responseService.getResumesByVacancyId(id, PageRequest.of(page, size));
        int totalResponses = responseService.getResponsesCountByVacancy(id);

        model.addAttribute("vacancy", vacancy);
        model.addAttribute("resumes", resumesPage);
        model.addAttribute("totalResponses", totalResponses);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "response/vacancy-responses";
    }
}