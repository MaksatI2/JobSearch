package org.example.JobSearch.dto;

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

    private String responsibilities;
}