package org.example.JobSearch.dto.register;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class EmployerRegisterDTO {

    @NotBlank(message = "Название компании обязательно")
    private String companyName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
    private String password;

    @NotBlank(message = "Номер телефона обязателен")
    private String phoneNumber;
}
