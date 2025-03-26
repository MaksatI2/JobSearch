package org.example.JobSearch.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ResumeDTO {

    @NotNull
    private Long id;

    @NotNull(message = "ID соискателя не может быть пустым")
    private Long applicantId;

    @NotNull(message = "ID категории не может быть пустым")
    private Long categoryId;

    @NotBlank
    @Size(min = 2, max = 100, message = "Название резюме должно быть от 2 до 100 символов")
    private String name;

    @NotNull
    @Min(value = 0, message = "Зарплата не может быть отрицательной")
    private Float salary;

    @NotNull
    private Boolean isActive;

    @NotNull(message = "Время обновления не может быть пустым")
    private Timestamp updateTime;

    private List<@Valid EducationInfoDTO> educationInfos;

    private List<@Valid WorkExperienceDTO> workExperiences;
}
