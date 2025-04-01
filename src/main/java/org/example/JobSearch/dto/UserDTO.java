package org.example.JobSearch.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.JobSearch.model.AccountType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неверный формат email")
    private String email;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String name;

    @NotBlank(message = "Фамилия не может быть пустым")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String surname;

    @Min(value = 18, message = "Возраст должен быть не менее 18 лет")
    @Max(value = 60, message = "Возраст должен быть не более 60 лет")
    @NotNull(message = "Возраст обязателен для заполнения")
    private Integer age;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Неверный формат номера телефона")
    private String phoneNumber;

    private String avatar;

    @NotNull(message = "Тип аккаунта обязателен для заполнения")
    @Pattern(regexp = "APPLICANT|EMPLOYER", message = "Тип аккаунта должен быть либо APPLICANT, либо EMPLOYER")
    private AccountType accountType;
}
