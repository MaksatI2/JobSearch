package org.example.JobSearch.model;

import lombok.*;

import java.security.Timestamp;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Resume {
    private Long id;
    private Long applicantId;
    private Long categoryId;

    private String name;
    private Float salary;
    private Boolean isActive;
    private Timestamp updateTime;
}