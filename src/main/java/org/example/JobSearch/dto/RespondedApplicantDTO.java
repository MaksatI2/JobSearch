package org.example.JobSearch.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RespondedApplicantDTO {
    private Long id;
    private Long resumeId;
    private Long vacancyId;
    private Boolean confirmation;
}
