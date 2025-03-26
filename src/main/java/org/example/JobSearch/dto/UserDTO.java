package org.example.JobSearch.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserDTO {

    @NotNull
    private Long id;

    @NotBlank
    @Email(message = "Некорректный email")
    private String email;

    @NotBlank
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String name;

    @NotBlank
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String surname;

    @NotNull
    @Min(value = 18, message = "Возраст должен быть не менее 18 лет")
    @Max(value = 60, message = "Возраст должен быть не более 60 лет")
    private Integer age;

    @NotBlank
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный номер телефона")
    private String phoneNumber;

    private String avatar;

    @NotBlank
    @Pattern(regexp = "^(APPLICANT|EMPLOYER)$", message = "Некорректный тип аккаунта")
    private String accountType;
}
