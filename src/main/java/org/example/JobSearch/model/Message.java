package org.example.JobSearch.model;

import lombok.*;

import java.sql.Timestamp;
@Getter
@Setter
public class Message {
    private Long id;
    private Long respondedApplicantsId;
    private String content;
    private Timestamp timestamp;
}
