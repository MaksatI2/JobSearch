package org.example.JobSearch.dto.register;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicantRegisterDTO {

    @NotBlank(message = "Имя обязательно")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s-]+$", message = "Имя содержит недопустимые символы")
    private String name;

    @NotBlank(message = "Фамилия обязательна")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s-]+$", message = "Фамилия содержит недопустимые символы")
    private String surname;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 18, message = "Минимальный возраст 18 лет")
    @Max(value = 50, message = "Максимальный возраст 50 лет")
    private Integer age;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
    private String password;

    @NotBlank(message = "Номер телефона обязателен")
    @Pattern(regexp = "^\\+?[0-9\\s-]{10,15}$", message = "Некорректный номер телефона")
    private String phoneNumber;
}
