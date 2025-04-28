package org.example.JobSearch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RespondedApplicantDTO {

    @NotNull(message = "ID не может быть пустым")
    private Long id;

    @NotNull(message = "ID резюме не может быть пустым")
    private Long resumeId;

    @NotNull(message = "ID вакансии не может быть пустым")
    private Long vacancyId;

    @NotNull(message = "Поле подтверждения не может быть пустым")
    private Boolean confirmation;

    @NotNull(message = "Поле просмотра не может быть пустым")
    private Boolean viewed;
}
