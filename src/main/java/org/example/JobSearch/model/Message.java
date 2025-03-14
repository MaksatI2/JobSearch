package org.example.JobSearch.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Message {
    private Integer id;
    private Integer respondedApplicantsId;

    private String content;
    private Timestamp timestamp;
}
