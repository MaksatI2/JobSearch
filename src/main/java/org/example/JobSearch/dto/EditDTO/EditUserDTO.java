package org.example.JobSearch.dto.EditDTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditUserDTO {

    Long id;

    @NotBlank(message = "{user.name.notBlank}")
    @Size(min = 2, max = 50, message = "{user.name.size}")
    private String name;

    @NotBlank(message = "{user.surname.notBlank}")
    @Size(min = 2, max = 50, message = "{user.surname.size}")
    private String surname;

    @Min(value = 18, message = "{user.age.min}")
    @Max(value = 60, message = "{user.age.max}")
    @NotNull(message = "{user.age.notNull}")
    private Integer age;

    private String avatar;

    @NotBlank(message = "{user.phone.notBlank}")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "{user.phone.invalid}")
    private String phoneNumber;
}
