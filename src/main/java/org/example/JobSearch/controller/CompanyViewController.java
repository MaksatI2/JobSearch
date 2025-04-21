package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyViewController {
    private final UserService userService;

    @GetMapping("/allCompanies")
    public String getAllCompanies(Model model) {
        List<UserDTO> companies = userService.getAllEmployers();

        companies.forEach(company -> {
            String avatarUrl = "/api/users/" + company.getId() + "/avatar";
            company.setAvatar(avatarUrl);
        });

        model.addAttribute("companies", companies);
        return "companies/allCompanies";
    }
}