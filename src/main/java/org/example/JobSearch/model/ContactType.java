package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactType {
    private Integer id;

    private String type;
}