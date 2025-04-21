package org.example.JobSearch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.exceptions.CreateVacancyException;
import org.example.JobSearch.exceptions.EditVacancyException;
import org.example.JobSearch.service.CategoryService;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.service.VacancyService;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacancyViewController {
    private final VacancyService vacancyService;
    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping
    public String showVacancies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort,
            Model model) {

        Page<VacancyDTO> vacanciesPage = vacancyService.getAllVacanciesSorted(
                sort,
                PageRequest.of(page, size)
        );

        model.addAttribute("vacancies", vacanciesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vacanciesPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedSort", sort);

        return "vacancies/vacancies";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("vacancyForm", new CreateVacancyDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "vacancies/createVacancy";
    }

    @PostMapping("/create")
    public String addVacancy(@Valid @ModelAttribute("vacancyForm") CreateVacancyDTO vacancyDTO,
                             BindingResult bindingResult,
                             Model model,
                             Principal principal) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "vacancies/createVacancy";
        }

        try {
            String currentUserEmail = principal.getName();
            Long currentUser = userService.getUserId(currentUserEmail);

            vacancyService.validateVacancyData(vacancyDTO, bindingResult);

            vacancyService.createVacancy(vacancyDTO, currentUser);

            return "redirect:/profile";

        } catch (CreateVacancyException e) {
            log.error("Create vacancy error: {}", e.getMessage());
            bindingResult.rejectValue(e.getFieldName(), "error.createVacancyDTO", e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "vacancies/createVacancy";
        }
    }


    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        VacancyDTO vacancy = vacancyService.getVacancyById(id);

        String currentUserEmail = principal.getName();
        Long currentUserId = userService.getUserId(currentUserEmail);

        if (!vacancy.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException("Вы не можете редактировать эту вакансию");
        }

        EditVacancyDTO form = vacancyService.convertToEditDTO(vacancy);
        model.addAttribute("vacancyForm", form);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("vacancyId", id);
        return "vacancies/editVacancy";
    }

    @PostMapping("/{id}/edit")
    public String editVacancy(@PathVariable Long id,
                              @Valid @ModelAttribute("vacancyForm") EditVacancyDTO form,
                              BindingResult bindingResult,
                              Model model,
                              Principal principal) {

        String currentUserEmail = principal.getName();
        Long currentUserId = userService.getUserId(currentUserEmail);

        VacancyDTO vacancy = vacancyService.getVacancyById(id);
        if (!vacancy.getAuthorId().equals(currentUserId)) {
            throw new AccessDeniedException("Вы не можете редактировать эту вакансию");
        }

        try {
            vacancyService.validateEditVacancyData(form, bindingResult);

            if (bindingResult.hasErrors()) {
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("vacancyId", id);
                return "vacancies/editVacancy";
            }

            form.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
            vacancyService.updateVacancy(id, form);

            return "redirect:/profile";

        } catch (EditVacancyException e) {
            log.error("Edit vacancy error: {}", e.getMessage());
            bindingResult.rejectValue(e.getFieldName(), "error.editVacancyDTO", e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("vacancyId", id);
            return "vacancies/editVacancy";
        }
    }


    @GetMapping("/{id}/info")
    public String showVacancyDetails(@PathVariable Long id, Model model, Principal principal) {
        VacancyDTO vacancy = vacancyService.getVacancyById(id);
        model.addAttribute("vacancy", vacancy);

        if (principal != null) {
            String currentUserEmail = principal.getName();
            Long currentUserId = userService.getUserId(currentUserEmail);
            model.addAttribute("isAuthor", vacancy.getAuthorId().equals(currentUserId));
        } else {
            model.addAttribute("isAuthor", false);
        }

        return "vacancies/vacancyInfo";
    }

    @PostMapping("/{id}/refresh")
    public String refreshVacancy(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        UserDTO user = userService.getUserByEmail(email);

        VacancyDTO vacancy = vacancyService.getVacancyById(id);
        if (!vacancy.getAuthorId().equals(user.getId())) {
            throw new AccessDeniedException("Вы можете обновлять только свои собственные вакансии.");
        }

        vacancyService.refreshVacancy(id);
        return "redirect:/profile?refreshSuccess";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
    }

}