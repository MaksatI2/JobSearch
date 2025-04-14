package org.example.JobSearch.dto.create;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVacancyDTO {

    @NotNull(message = "ID автора вакансии не может быть пустым")
    @Positive(message = "ID автора должен быть положительным числом")
    private Long authorId;

    @NotNull(message = "ID категории не может быть пустым")
    @Positive(message = "ID категории должен быть положительным числом")
    private Long categoryId;

    @NotBlank(message = "Название вакансии не может быть пустым")
    @Size(min = 2, max = 100, message = "Название вакансии должно быть от 2 до 100 символов")
    private String name;

    @NotBlank(message = "Описание обязательно")
    @Size(min = 10, max = 1000, message = "Описание должно быть от 10 до 1000 символов")
    private String description;

    @NotNull(message = "Зарплата обязательна")
    @Positive(message = "Зарплата не может быть отрицательной")
    @Digits(integer = 10, fraction = 2, message = "Введите корректную зарплату")
    private Float salary;

    @NotNull(message = "Укажите минимальный опыт")
    @Min(value = 0, message = "Опыт не может быть меньше 0")
    @Max(value = 50, message = "Опыт не может быть больше 50")
    private Integer expFrom;

    @NotNull(message = "Укажите максимальный опыт")
    @Min(value = 0, message = "Опыт не может быть меньше 0")
    @Max(value = 50, message = "Опыт не может быть больше 50")
    private Integer expTo;

    @NotNull(message = "Статус обязателен")
    private Boolean isActive;
}