package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class EducationInfo {
    private Long id;
    private Long resumeId;

    private String institution;
    private String program;
    private Date startDate;
    private Date endDate;
    private String degree;
}