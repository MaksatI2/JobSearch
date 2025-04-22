package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyViewController {
    private final UserService userService;

    @GetMapping("/allCompanies")
    public String getAllCompanies(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  Model model) {
        Page<UserDTO> companyPage = userService.getAllEmployers(PageRequest.of(page, size));

        companyPage.getContent().forEach(company -> {
            String avatarUrl = "/api/users/" + company.getId() + "/avatar";
            company.setAvatar(avatarUrl);
        });

        model.addAttribute("companies", companyPage.getContent());
        model.addAttribute("totalPages", companyPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "companies/allCompanies";
    }

}