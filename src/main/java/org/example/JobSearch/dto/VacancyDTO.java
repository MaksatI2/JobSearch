package org.example.JobSearch.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VacancyDTO {

    @NotNull
    private Long id;

    @NotNull(message = "ID автора вакансии не может быть пустым")
    private Long authorId;

    @NotNull(message = "ID категории не может быть пустым")
    private Long categoryId;

    @NotBlank
    @Size(min = 2, max = 100, message = "Название вакансии должно быть от 2 до 100 символов")
    private String name;

    @NotBlank
    @Size(min = 10, max = 1000, message = "Описание вакансии должно быть от 10 до 1000 символов")
    private String description;

    @NotNull
    @Min(value = 0, message = "Зарплата не может быть отрицательной")
    private Float salary;

    @NotNull
    @Min(value = 0, message = "Минимальный опыт не может быть отрицательным")
    private Integer expFrom;

    @NotNull
    @Min(value = 0, message = "Максимальный опыт не может быть отрицательным")
    @Max(value = 50, message = "Максимальный опыт не может превышать 50 лет")
    private Integer expTo;

    @NotNull
    private Boolean isActive;

    @NotNull(message = "Дата создания не может быть пустой")
    private LocalDate createdDate;

    @NotNull(message = "Дата обновления не может быть пустой")
    private LocalDate updateTime;
}
