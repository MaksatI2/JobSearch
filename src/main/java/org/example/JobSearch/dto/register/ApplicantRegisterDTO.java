package org.example.JobSearch.dto.register;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class ApplicantRegisterDTO {

    @NotBlank(message = "Имя обязательно")
    private String name;

    @NotBlank(message = "Фамилия обязательна")
    private String surname;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 18, message = "Минимальный возраст 18 лет")
    private Integer age;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
    private String password;

    @NotBlank(message = "Номер телефона обязателен")
    private String phoneNumber;
}
