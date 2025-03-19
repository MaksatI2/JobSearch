package org.example.JobSearch.model;

import lombok.*;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Message {
    private Long id;
    private Long respondedApplicantsId;

    private String content;
    private Timestamp timestamp;
}
