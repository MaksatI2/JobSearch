package org.example.JobSearch.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RespondedApplicantDTO {
    private Long id;
    private Long resumeId;
    private Long vacancyId;
    private Boolean confirmation;
}
