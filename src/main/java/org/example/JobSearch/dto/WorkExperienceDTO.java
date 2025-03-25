package org.example.JobSearch.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WorkExperienceDTO {
    private Long id;
    private Long resumeId;
    private Long years;
    private String companyName;
    private String position;
    private String responsibilities;
}