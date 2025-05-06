package org.example.JobSearch.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.exceptions.InvalidRegisterException;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.service.OAuth2RegistrationService;
import org.example.JobSearch.service.oauth2.GoogleUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2RegistrationService registrationService;

    @GetMapping("/account-type")
    public String chooseAccountType(Model model, HttpSession session) {
        GoogleUserInfo userInfo = (GoogleUserInfo) session.getAttribute("oauth2UserInfo");
        if (userInfo == null) return "redirect:/auth/login";

        model.addAttribute("email", userInfo.getEmail());
        model.addAttribute("name", userInfo.getName());
        return "auth/choose-account-type";
    }

    @PostMapping("/account-type")
    public String setAccountType(@RequestParam("accountType") Integer accountTypeId, HttpSession session) {
        GoogleUserInfo userInfo = (GoogleUserInfo) session.getAttribute("oauth2UserInfo");
        if (userInfo == null) return "redirect:/auth/login";

        AccountType accountType = AccountType.getById(accountTypeId);
        session.setAttribute("selectedAccountType", accountType);
        return "redirect:/auth/oauth2/complete-profile";
    }

    @GetMapping("/complete-profile")
    public String completeProfile(Model model, HttpSession session) {
        GoogleUserInfo userInfo = (GoogleUserInfo) session.getAttribute("oauth2UserInfo");
        AccountType accountType = (AccountType) session.getAttribute("selectedAccountType");

        if (userInfo == null || accountType == null) return "redirect:/auth/login";

        model.addAttribute("accountType", accountType);
        model.addAttribute("userInfo", userInfo);
        return "auth/complete-profile";
    }

    @PostMapping("/complete-profile")
    public String saveCompletedProfile(@RequestParam("phoneNumber") String phoneNumber,
                                       @RequestParam(value = "age", required = false) Integer age,
                                       @RequestParam(value = "surname", required = false) String surname,
                                       HttpSession session, Model model) {
        GoogleUserInfo userInfo = (GoogleUserInfo) session.getAttribute("oauth2UserInfo");
        AccountType accountType = (AccountType) session.getAttribute("selectedAccountType");

        if (userInfo == null || accountType == null) return "redirect:/auth/login";

        try {
            registrationService.saveUserFromOAuth(userInfo, accountType, phoneNumber, age, surname);
            session.removeAttribute("oauth2UserInfo");
            session.removeAttribute("selectedAccountType");

            return (accountType == AccountType.APPLICANT) ? "redirect:/vacancies" : "redirect:/resumes/allResumes";
        } catch (InvalidRegisterException e) {
            model.addAttribute("phoneNumber", phoneNumber);
            if (accountType == AccountType.APPLICANT) {
                model.addAttribute("age", age);
                model.addAttribute("surname", surname);
            }
            model.addAttribute("error", e.getMessage());
            model.addAttribute("errorField", e.getFieldName());
            model.addAttribute("accountType", accountType);
            model.addAttribute("userInfo", userInfo);

            return "auth/complete-profile";
        }
    }
}