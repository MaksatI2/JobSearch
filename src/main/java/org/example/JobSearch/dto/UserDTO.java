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

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    private String surname;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 60, message = "Age must be at most 60")
    @NotNull(message = "Age is required")
    private Integer age;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    private String avatar;

    @NotNull(message = "Account type is required")
    private AccountType accountType;
}
