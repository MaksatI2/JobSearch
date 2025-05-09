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

    @NotBlank(message = "{education.institution.notBlank}")
    @Size(min = 2, max = 100, message = "{education.institution.size}")
    private String institution;

    @NotBlank(message = "{education.program.notBlank}")
    @Size(min = 2, max = 100, message = "{education.program.size}")
    private String program;

    @NotNull(message = "{education.startDate.notNull}")
    @Past(message = "{education.startDate.past}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull(message = "{education.endDate.notNull}")
    @PastOrPresent(message = "{education.endDate.pastOrPresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @NotBlank(message = "{education.degree.notBlank}")
    @Size(min = 2, max = 50, message = "{education.degree.size}")
    private String degree;
}
