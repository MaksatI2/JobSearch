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

@Slf4j
@Controller
@RequestMapping("/response")
@RequiredArgsConstructor
public class ResponseViewController {

    private final ResponseService responseService;
    private final ResumeService resumeService;
    private final UserService userService;
    private final VacancyService vacancyService;
    private final MessageSource messageSource;

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
            throw new AccessDeniedException(getMessage("response.vacancy.forbidden"));
        }

        Page<ResumeDTO> resumesPage = responseService.getResumesByVacancyId(id, PageRequest.of(page, size));
        int totalResponses = responseService.getResponsesCountByVacancy(id);

        model.addAttribute("vacancy", vacancy);
        model.addAttribute("resumes", resumesPage);
        model.addAttribute("totalResponses", totalResponses);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "responses/vacancy-responses";
    }

    @PostMapping("/respond")
    public String respondToVacancy(@RequestParam Long resumeId,
                                   @RequestParam Long vacancyId,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {
        try {
            ResumeDTO resume = resumeService.getResumeById(resumeId);
            String email = principal.getName();
            UserDTO user = userService.getUserByEmail(email);

            if (!resume.getApplicantId().equals(user.getId())) {
                throw new AccessDeniedException(getMessage("response.resume.forbidden"));
            }

            responseService.respondToVacancy(resumeId, vacancyId);
            redirectAttributes.addFlashAttribute("successMessage", getMessage("response.respond"));
            return "redirect:/vacancies/" + vacancyId + "/info?success=true";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/vacancies/" + vacancyId + "/info?error=true";
        }
    }

    @GetMapping("/resumes/{id}")
    public String responsesByResume(@PathVariable Long id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    Model model,
                                    Principal principal) {

        ResumeDTO resume = resumeService.getResumeById(id);
        String email = principal.getName();
        UserDTO user = userService.getUserByEmail(email);

        if (!resume.getApplicantId().equals(user.getId())) {
            throw new AccessDeniedException(getMessage("response.resume.forbidden"));
        }

        Page<VacancyDTO> vacanciesPage = responseService.getVacanciesByResumeId(id, PageRequest.of(page, size));
        int totalResponses = responseService.getResponsesCountByResume(id);

        model.addAttribute("resume", resume);
        model.addAttribute("vacancies", vacanciesPage);
        model.addAttribute("totalResponses", totalResponses);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "responses/resume-responses";
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}