package org.example.JobSearch.dto.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.JobSearch.dto.ContactInfoDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateResumeDTO {

    Long id;

    private Long applicantId;

    @NotNull(message = "ID категории не может быть пустым")
    @Positive(message = "ID категории должен быть положительным числом")
    private Long categoryId;

    @NotBlank(message = "Название резюме не может быть пустым")
    @Size(min = 2, max = 100, message = "Название резюме должно быть от 2 до 100 символов")
    private String name;

    @NotNull(message = "Зарплата не может быть пустой")
    @Min(value = 0, message = "Зарплата не может быть отрицательной")
    private Float salary;

    @NotNull(message = "Статус активности не может быть пустым")
    private Boolean isActive;

    private Timestamp createDate;

    private Timestamp updateTime;

    @Valid
    private List<EducationInfoDTO> educationInfos;

    @Valid
    private List<WorkExperienceDTO> workExperiences;

    private List<ContactInfoDTO> contactInfos;
}
