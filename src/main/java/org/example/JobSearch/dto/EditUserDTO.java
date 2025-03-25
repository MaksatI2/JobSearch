package org.example.JobSearch.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EditUserDTO {
    private String name;
    private String surname;
    private Integer age;
    private String phoneNumber;
}
