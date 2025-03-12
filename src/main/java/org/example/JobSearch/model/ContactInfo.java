package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactInfo {
    private Integer id;
    private Integer resumeId;
    private Integer typeId;
    private String value;
}