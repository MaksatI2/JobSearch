package org.example.JobSearch.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
    private LocalDate createdDate;
    private LocalDate updateTime;
}

