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

    @NotNull(message = "{resume.categoryId.notNull}")
    @Positive(message = "{resume.categoryId.positive}")
    private Long categoryId;

    @NotBlank(message = "{resume.name.notBlank}")
    @Size(min = 2, max = 100, message = "{resume.name.size}")
    private String name;

    @NotNull(message = "{resume.salary.notNull}")
    @Min(value = 0, message = "{resume.salary.min}")
    private Float salary;

    @NotNull(message = "{resume.isActive.notNull}")
    private Boolean isActive;

    private Timestamp createDate;

    private Timestamp updateTime;

    @Valid
    private List<EducationInfoDTO> educationInfos;

    @Valid
    private List<WorkExperienceDTO> workExperiences;

    private List<ContactInfoDTO> contactInfos;
}
