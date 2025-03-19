package org.example.JobSearch.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ContactInfo {
    private Long id;
    private Long resumeId;
    private Long typeId;
    private String value;
}