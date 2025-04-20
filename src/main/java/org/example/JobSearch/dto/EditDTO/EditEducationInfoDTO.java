package org.example.JobSearch.dto.EditDTO;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditEducationInfoDTO {

    private  Long id;

    @NotBlank
    @Size(min = 2, max = 100, message = "Название учебного заведения должно быть от 2 до 100 символов")
    private String institution;

    @NotBlank
    @Size(min = 2, max = 100, message = "Название программы должно быть от 2 до 100 символов")
    private String program;

    @NotNull(message = "Дата не может быть пустым")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull(message = "Дата не может быть пустым")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @NotBlank
    @Size(min = 2, max = 50, message = "Степень должна быть от 2 до 50 символов")
    private String degree;
}
