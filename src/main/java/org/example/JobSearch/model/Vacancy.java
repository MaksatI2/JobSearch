package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Vacancy {
    private Integer id;
    private String name;
    private String description;
    private Integer categoryId;
    private Float salary;
    private LocalDate expFrom;
    private LocalDate expTo;
    private Boolean isActive;
    private Integer authorId;
    private LocalDate createdDate;
    private LocalDate updateTime;
}

