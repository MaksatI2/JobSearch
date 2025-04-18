package org.example.JobSearch.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class WorkExperienceDTO {

    private Long id;

    private Long resumeId;

    @NotNull
    @Min(value = 0, message = "Количество лет опыта не может быть отрицательным")
    @Max(value = 50, message = "Количество лет опыта не может превышать 50 лет")
    private Integer years;

    @NotBlank
    @Size(min = 2, max = 100, message = "Название компании должно быть от 2 до 100 символов")
    private String companyName;

    @NotBlank
    @Size(min = 2, max = 100, message = "Название должности должно быть от 2 до 100 символов")
    private String position;

    @NotBlank
    @Size(min = 10, max = 1000, message = "Описание обязанностей должно быть от 10 до 1000 символов")
    private String responsibilities;
}
