package org.example.JobSearch.dto.register;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicantRegisterDTO {

    @NotBlank(message = "{user.name.notBlank}")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s-]+$", message = "{user.name.size}")
    private String name;

    @NotBlank(message = "{user.surname.notBlank}")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s-]+$", message = "{user.surname.size}")
    private String surname;

    @Min(value = 18, message = "{user.age.min}")
    @Max(value = 60, message = "{user.age.max}")
    @NotNull(message = "{user.age.notNull}")
    private Integer age;

    @NotBlank(message = "{user.email.notBlank}")
    @Email(message = "{user.email.invalid}")
    private String email;

    @NotBlank(message = "{user.password.notBlank}")
    @Size(min = 6, message = "{user.password.size}")
    private String password;

    @NotBlank(message = "{user.phone.notBlank}")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "{user.phone.invalid}")
    private String phoneNumber;
}
