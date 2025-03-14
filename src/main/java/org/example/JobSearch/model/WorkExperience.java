package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkExperience {
    private Integer id;
    private Integer resumeId;

    private Integer years;
    private String companyName;
    private String position;
    private String responsibilities;
}
