package org.example.JobSearch.dto.EditDTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditVacancyDTO {

    Long id;

    @NotNull(message = "{vacancy.categoryId.notNull}")
    @Positive(message = "{vacancy.categoryId.positive}")
    private Long categoryId;

    @NotBlank(message = "{vacancy.name.notBlank}")
    @Size(min = 2, max = 100, message = "{vacancy.name.size}")
    private String name;

    @NotBlank(message = "{vacancy.description.notBlank}")
    @Size(min = 10, max = 1000, message = "{vacancy.description.size}")
    private String description;

    @NotNull(message = "{vacancy.salary.notNull}")
    @Positive(message = "{vacancy.salary.min}")
    private Float salary;

    @NotNull(message = "{vacancy.expFrom.notNull}")
    @Min(value = 0, message = "{vacancy.expFrom.min}")
    private Integer expFrom;

    @NotNull(message = "{vacancy.expTo.notNull}")
    @Min(value = 0, message = "{vacancy.expTo.min}")
    @Max(value = 50, message = "{vacancy.expTo.max}")
    private Integer expTo;

    private String responsibilities;

    private Long regionId;

    private List<Long> workScheduleIds;

    private List<Long> employmentTypeIds;

    @NotNull
    private Boolean isActive;

    private Timestamp updateTime;
}
