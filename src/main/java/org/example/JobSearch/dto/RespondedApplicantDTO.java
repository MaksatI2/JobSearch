package org.example.JobSearch.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RespondedApplicantDTO {
    private Integer id;
    private Integer resumeId;
    private Integer vacancyId;
    private Boolean confirmation;
}
