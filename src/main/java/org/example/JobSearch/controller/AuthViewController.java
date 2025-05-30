package org.example.JobSearch.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.register.ApplicantRegisterDTO;
import org.example.JobSearch.dto.register.EmployerRegisterDTO;
import org.example.JobSearch.dto.register.ResetPasswordRequest;
import org.example.JobSearch.exceptions.InvalidRegisterException;
import org.example.JobSearch.exceptions.UserNotFoundException;
import org.example.JobSearch.model.User;
import org.example.JobSearch.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("applicantRegisterDTO")) {
            model.addAttribute("applicantRegisterDTO", new ApplicantRegisterDTO());
        }
        if (!model.containsAttribute("employerRegisterDTO")) {
            model.addAttribute("employerRegisterDTO", new EmployerRegisterDTO());
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
            model.addAttribute("employerRegisterDTO", new EmployerRegisterDTO());
            return "auth/register";
        }

        try {
            userService.registerApplicant(applicantDto);
            return "redirect:/auth/login?registered";
        } catch (InvalidRegisterException e) {
            log.error("Registration error: {}", e.getMessage());
            bindingResult.rejectValue(e.getFieldName(), "error.applicantRegisterDTO", e.getMessage());
            model.addAttribute("employerRegisterDTO", new EmployerRegisterDTO());
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
            model.addAttribute("applicantRegisterDTO", new ApplicantRegisterDTO());
            return "auth/register";
        }

        try {
            userService.registerEmployer(employerDto);
            return "redirect:/auth/login?registered";
        } catch (InvalidRegisterException e) {
            log.error("Registration error: {}", e.getMessage());
            bindingResult.rejectValue(e.getFieldName(), "error.employerRegisterDTO", e.getMessage());
            model.addAttribute("applicantRegisterDTO", new ApplicantRegisterDTO());
            return "auth/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "registered", required = false) String registered,
            Model model) {

        if (error != null) {
            model.addAttribute("loginError", getMessage("login.error"));
        }

        if (registered != null) {
            model.addAttribute("registrationSuccess", getMessage("register.success"));
        }

        return "auth/login";
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "auth/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");

        try {
            String token = userService.generateResetPasswordToken(email);
            String resetLink = getBaseUrl(request) + "/auth/reset_password?token=" + token;

            model.addAttribute("token", token);
            model.addAttribute("resetLink", resetLink);
            model.addAttribute("message", getMessage("password.reset.token.generated"));
        } catch (UserNotFoundException ex) {
            model.addAttribute("error", getMessage("password.reset.error.user_not_found"));
        }

        return "auth/forgot_password_form";
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken(token);
        model.addAttribute("resetRequest", request);
        return "auth/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(
            @ModelAttribute("resetRequest") @Valid ResetPasswordRequest resetRequest,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/reset_password_form";
        }

        try {
            User user = userService.getByResetPasswordToken(resetRequest.getToken());
            userService.updatePassword(user, resetRequest.getPassword());
            model.addAttribute("registrationSuccess", getMessage("password.reset.success"));
            return "auth/login";
        } catch (UserNotFoundException e) {
            bindingResult.rejectValue("token", "invalid.token", getMessage("password.reset.error.invalid_token"));
            return "auth/reset_password_form";
        }
    }

    private String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}