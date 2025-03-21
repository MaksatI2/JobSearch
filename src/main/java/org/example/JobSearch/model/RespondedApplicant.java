package org.example.JobSearch.model;

import lombok.*;

@Getter
@Setter
public class RespondedApplicant {
    private Long id;
    private Long resumeId;
    private Long vacancyId;
    private Boolean confirmation;
}
