package org.example.JobSearch.dto.EditDTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditVacancyDTO {

    Long id;

    @NotNull(message = "ID автора вакансии не может быть пустым")
    @Positive(message = "ID автора должен быть положительным числом")
    private Long categoryId;

    @NotBlank(message = "Название вакансии не может быть пустым")
    @Size(min = 2, max = 100, message = "Название вакансии должно быть от 2 до 100 символов")
    private String name;

    @NotBlank(message = "Описание вакансии не может быть пустым")
    @Size(min = 10, max = 1000, message = "Описание вакансии должно быть от 10 до 1000 символов")
    private String description;

    @NotNull(message = "Зарплата не может быть пустой")
    @Min(value = 0, message = "Зарплата не может быть отрицательной")
    private Float salary;

    @NotNull(message = "Минимальный опыт не может быть пустым")
    @Min(value = 0, message = "Минимальный опыт не может быть отрицательным")
    private Integer expFrom;

    @NotNull(message = "Максимальный опыт не может быть пустым")
    @Min(value = 0, message = "Максимальный опыт не может быть отрицательным")
    @Max(value = 50, message = "Максимальный опыт не может превышать 50 лет")
    private Integer expTo;

    @NotNull
    private Boolean isActive;

    private Timestamp updateTime;
}
