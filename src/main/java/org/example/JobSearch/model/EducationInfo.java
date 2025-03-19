package org.example.JobSearch.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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