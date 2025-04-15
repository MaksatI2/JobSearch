package org.example.JobSearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.register.ApplicantRegisterDTO;
import org.example.JobSearch.dto.register.EmployerRegisterDTO;
import org.example.JobSearch.exceptions.InvalidRegisterException;
import org.example.JobSearch.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthViewController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(
            @RequestParam(value = "type", defaultValue = "applicant") String type,
            Model model) {

        model.addAttribute("type", type);

        if ("applicant".equals(type)) {
            if (!model.containsAttribute("applicantRegisterDTO")) {
                model.addAttribute("applicantRegisterDTO", new ApplicantRegisterDTO());
            }
        } else {
            if (!model.containsAttribute("employerRegisterDTO")) {
                model.addAttribute("employerRegisterDTO", new EmployerRegisterDTO());
            }
        }

        return "auth/register";
    }

    @PostMapping("/register/applicant")
    public String registerApplicant(
            @Valid @ModelAttribute("applicantRegisterDTO") ApplicantRegisterDTO applicantDto,
            BindingResult bindingResult,
            Model model) {

        log.info("Registering applicant: {}", applicantDto);

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("type", "applicant");
            return "auth/register";
        }

        try {
            userService.registerApplicant(applicantDto);
            return "redirect:/auth/login?registered";
        } catch (InvalidRegisterException e) {
            log.error("Registration error: {}", e.getMessage());
            model.addAttribute("type", "applicant");
            bindingResult.rejectValue(e.getFieldName(), "error.applicantRegisterDTO", e.getMessage());
            return "auth/register";
        }
    }

    @PostMapping("/register/employer")
    public String registerEmployer(
            @Valid @ModelAttribute("employerRegisterDTO") EmployerRegisterDTO employerDto,
            BindingResult bindingResult,
            Model model) {

        log.info("Registering employer: {}", employerDto);

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("type", "employer");
            return "auth/register";
        }

        try {
            userService.registerEmployer(employerDto);
            return "redirect:/auth/login?registered";
        } catch (InvalidRegisterException e) {
            log.error("Registration error: {}", e.getMessage());
            model.addAttribute("type", "employer");
            bindingResult.rejectValue(e.getFieldName(), "error.employerRegisterDTO", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "registered", required = false) String registered,
            Model model) {

        if (error != null) {
            model.addAttribute("loginError", "Неверный email или пароль");
        }

        if (registered != null) {
            model.addAttribute("registrationSuccess", "Регистрация прошла успешно! Войдите в систему.");
        }

        return "auth/login";
    }
}