package org.example.JobSearch.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkExperienceDTO {

    private Long id;
    private Long resumeId;

    @NotNull
    @Min(value = 0, message = "{experience.years.min}")
    @Max(value = 50, message = "{experience.years.max}")
    private Integer years;

    @NotNull
    @Min(value = 0, message = "{experience.months.min}")
    @Max(value = 11, message = "{experience.months.max}")
    private Integer months;

    @NotBlank(message = "{experience.companyName.notBlank}")
    @Size(min = 2, max = 100, message = "{experience.companyName.size}")
    private String companyName;

    @NotBlank(message = "{experience.position.notBlank}")
    @Size(min = 2, max = 100, message = "{experience.position.size}")
    private String position;

    @NotBlank(message = "{experience.responsibilities.notBlank}")
    @Size(min = 10, max = 1000, message = "{experience.responsibilities.size}")
    private String responsibilities;
}