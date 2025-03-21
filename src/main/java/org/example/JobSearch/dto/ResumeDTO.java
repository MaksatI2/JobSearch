package org.example.JobSearch.dto;

import lombok.*;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
