package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EditDTO.EditUserDTO;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.service.EditUserService;
import org.example.JobSearch.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class EditUserController {
    private final EditUserService editUserService;
    private final UserService userService;

    @GetMapping("/edit")
    public String showEditProfileForm(Model model, Principal principal) {
        String email = principal.getName();
        UserDTO user = userService.getUserByEmail(email);

        EditUserDTO editUserDTO = EditUserDTO.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .phoneNumber(user.getPhoneNumber())
                .build();

        model.addAttribute("editUserDTO", editUserDTO);
        return "user/editProfile";
    }

    @PostMapping("/edit")
    public String updateProfile(
            @Valid @ModelAttribute("editUserDTO") EditUserDTO editUserDTO,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "user/editProfile";
        }

        try {
            editUserService.updateUserByEmail(principal.getName(), editUserDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Профиль успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении профиля: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    @GetMapping("/avatar/edit")
    public String showEditAvatarForm(Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        UserDTO user = userService.getUserByEmail(currentUserEmail);

        model.addAttribute("user", user);

        return "user/editAvatar";
    }

    @PostMapping("/avatar")
    public String updateAvatar(@RequestParam("file") MultipartFile file,
                               @RequestParam("userId") Long userId,
                               RedirectAttributes redirectAttributes) {
        try {
            editUserService.updateUserAvatar(userId, file);
            redirectAttributes.addFlashAttribute("successMessage", "Аватар успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении аватара: " + e.getMessage());
        }

        return "redirect:/profile";
    }
}