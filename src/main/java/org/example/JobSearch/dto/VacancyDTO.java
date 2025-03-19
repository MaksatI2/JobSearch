package org.example.JobSearch.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class VacancyDTO {
    private Long id;
    private Long authorId;
    private Long categoryId;
    private String name;
    private String description;
    private Float salary;
    private LocalDate expFrom;
    private LocalDate expTo;
    private Boolean isActive;
}