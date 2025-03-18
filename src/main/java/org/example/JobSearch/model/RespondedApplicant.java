package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RespondedApplicant {
    private Long id;
    private Long resumeId;
    private Long vacancyId;

    private Boolean confirmation;
}
