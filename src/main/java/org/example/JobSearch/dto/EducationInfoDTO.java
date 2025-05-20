package org.example.JobSearch.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationInfoDTO {

    private Long id;

    private Long resumeId;

    private String institution;

    private String program;

    @Past(message = "{education.startDate.past}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @PastOrPresent(message = "{education.endDate.pastOrPresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String degree;
}
