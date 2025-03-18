package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Vacancy {
    private Long id;
    private Long authorId;
    private Long categoryId;

    private String name;
    private String description;
    private Float salary;
    private LocalDate expFrom;
    private LocalDate expTo;
    private Boolean isActive;
    private LocalDate createdDate;
    private LocalDate updateTime;
}

