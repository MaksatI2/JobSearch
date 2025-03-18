package org.example.JobSearch.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ResumeDTO {
    private Long id;
    private Long applicantId;
    private Long categoryId;
    private String name;
    private Float salary;
    private Boolean isActive;
    private Timestamp updateTime;
}
