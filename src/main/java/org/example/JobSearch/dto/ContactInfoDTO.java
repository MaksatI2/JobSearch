package org.example.JobSearch.dto;

import lombok.Data;

@Data
public class ContactInfoDTO {
    private Long id;
    private Long typeId;
    private String typeName;
    private String value;
}