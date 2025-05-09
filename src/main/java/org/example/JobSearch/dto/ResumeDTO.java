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

    Long id;

    @NotNull(message = "{resume.applicantId.notNull}")
    @Positive(message = "{resume.applicantId.positive}")
    private Long applicantId;

    private String applicantName;
    private Integer applicantAge;
    private String applicantAvatar;
    private Integer responsesCount;

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

    @NotNull(message = "{resume.createDate.notNull}")
    private Timestamp createDate;

    @NotNull(message = "{resume.updateTime.notNull}")
    private Timestamp updateTime;

    @Valid
    private List<EducationInfoDTO> educationInfos;

    @Valid
    private List<WorkExperienceDTO> workExperiences;

    private List<ContactInfoDTO> contactInfos;
}
