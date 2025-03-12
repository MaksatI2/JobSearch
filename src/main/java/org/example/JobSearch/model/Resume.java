package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

import java.security.Timestamp;

@Data
@Builder
public class Resume {
    private Integer id;
    private Integer applicantId;
    private String name;
    private Integer categoryId;
    private Float salary;
    private Boolean isActive;
    private Timestamp updateTime;
}