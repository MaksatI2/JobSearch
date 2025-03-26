package org.example.JobSearch.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditUserDTO {

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
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Номер телефона должен содержать от 10 до 15 цифр")
    private String phoneNumber;
}
