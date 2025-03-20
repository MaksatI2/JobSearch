package org.example.JobSearch.model;

import lombok.*;

@Getter
@Setter
public class WorkExperience {
    private  Long id;
    private Long resumeId;
    private Long years;
    private String companyName;
    private String position;
    private String responsibilities;
}
