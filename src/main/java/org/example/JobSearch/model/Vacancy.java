package org.example.JobSearch.model;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class Vacancy {
    private Long id;
    private Long authorId;
    private Long categoryId;
    private String name;
    private String description;
    private Float salary;
    private Integer expFrom;
    private Integer expTo;
    private Boolean isActive;
    private Timestamp createdDate;
    private Timestamp updateTime;
}

