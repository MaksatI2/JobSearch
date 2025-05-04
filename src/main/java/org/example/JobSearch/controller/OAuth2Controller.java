package org.example.JobSearch.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.model.User;
import org.example.JobSearch.repository.UserRepository;
import org.example.JobSearch.service.oauth2.GoogleUserInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.UUID;

@Controller
@RequestMapping("/auth/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/account-type")
    public String chooseAccountType(Model model, HttpSession session) {
        GoogleUserInfo userInfo = (GoogleUserInfo) session.getAttribute("oauth2UserInfo");

        if (userInfo == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("email", userInfo.getEmail());
        model.addAttribute("name", userInfo.getName());
        return "auth/choose-account-type";
    }

    @PostMapping("/account-type")
    public String setAccountType(@RequestParam("accountType") Integer accountTypeId,
                                 HttpSession session) {
        GoogleUserInfo userInfo = (GoogleUserInfo) session.getAttribute("oauth2UserInfo");

        if (userInfo == null) {
            return "redirect:/auth/login";
        }
        AccountType accountType = AccountType.getById(accountTypeId);

        User newUser = User.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .surname(userInfo.getSurname() != null ? userInfo.getSurname() : "")
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .accountType(accountType)
                .enabled(true)
                .phoneNumber("Не указан")
                .avatar(userInfo.getPicture())
                .build();

        userRepository.save(newUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userInfo.getEmail(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(accountType.getName()))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        session.removeAttribute("oauth2UserInfo");

        if (accountType == AccountType.APPLICANT) {
            return "redirect:/users/profile";
        } else {
            return "redirect:/users/profile";
        }
    }
}