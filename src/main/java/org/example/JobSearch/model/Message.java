package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Message {
    private Long id;
    private Long respondedApplicantsId;

    private String content;
    private Timestamp timestamp;
}
