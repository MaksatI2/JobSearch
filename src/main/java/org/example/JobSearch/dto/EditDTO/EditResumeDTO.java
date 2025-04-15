package org.example.JobSearch.dto.EditDTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class EditResumeDTO {

    private Long id;

    private Long applicantId;

    @NotNull(message = "ID категории не может быть пустым")
    @Positive(message = "ID категории должен быть положительным числом")
    private Long categoryId;

    @NotBlank(message = "Название резюме не может быть пустым")
    @Size(min = 2, max = 100, message = "Название резюме должно быть от 2 до 100 символов")
    private String name;

    @NotNull(message = "Зарплата не может быть пустой")
    @Min(value = 0, message = "Зарплата не может быть отрицательной")
    private Float salary;

    @NotNull(message = "Статус активности не может быть пустым")
    private Boolean isActive;

    private Timestamp updateTime;
}