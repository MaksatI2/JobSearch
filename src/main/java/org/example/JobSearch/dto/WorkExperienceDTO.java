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

    private Integer years;

    private Integer months;

    private String companyName;

    private String position;

    @Size(min = 10, max = 1000, message = "{experience.responsibilities.size}")
    private String responsibilities;
}