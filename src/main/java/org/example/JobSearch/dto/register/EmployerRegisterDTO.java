package org.example.JobSearch.dto.register;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerRegisterDTO {

    @NotBlank(message = "{validation.user.companyName.notBlank}")
    private String companyName;

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
