package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkExperience {
    private  Long id;
    private Long resumeId;

    private Long years;
    private String companyName;
    private String position;
    private String responsibilities;
}
