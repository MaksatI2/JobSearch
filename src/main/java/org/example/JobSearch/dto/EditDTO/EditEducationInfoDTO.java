package org.example.JobSearch.dto.EditDTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditEducationInfoDTO {

    private  Long id;

    @NotBlank
    @Size(min = 2, max = 100, message = "Название учебного заведения должно быть от 2 до 100 символов")
    private String institution;

    @NotBlank
    @Size(min = 2, max = 100, message = "Название программы должно быть от 2 до 100 символов")
    private String program;

    @NotNull(message = "Дата начала обучения не может быть пустой")
    @PastOrPresent(message = "Дата начала обучения не может быть в будущем")
    private Date startDate;

    @NotNull(message = "Дата окончания обучения не может быть пустой")
    @FutureOrPresent(message = "Дата окончания обучения не может быть в прошлом")
    private Date endDate;

    @NotBlank
    @Size(min = 2, max = 50, message = "Степень должна быть от 2 до 50 символов")
    private String degree;
}
