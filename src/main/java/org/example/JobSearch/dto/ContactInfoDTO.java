package org.example.JobSearch.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactInfoDTO {
    private Long id;
    private Long typeId;
    private String typeName;
    private String value;
}