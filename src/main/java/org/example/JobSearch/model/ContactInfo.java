package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactInfo {
    private Long id;
    private Long resumeId;
    private Long typeId;

    private String value;
}