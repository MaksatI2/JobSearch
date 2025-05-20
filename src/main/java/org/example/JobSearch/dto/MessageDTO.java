package org.example.JobSearch.dto;

import lombok.*;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.model.Message;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {

    private Long id;
    private Long respondedApplicantId;
    private String content;
    private Timestamp sendTime;
    private AccountType senderType;
    private boolean isRead;

    private Long applicantId;
    private String applicantFirstName;
    private String applicantLastName;
    private String applicantEmail;
    private String applicantAvatar;

    private Long employerId;
    private String employerCompanyName;
    private String employerEmail;
    private String employerAvatar;

    private Long resumeId;
    private String resumeTitle;

    private Long vacancyId;
    private String vacancyTitle;

    private String lastMessageContent;
    private Timestamp lastMessageTime;
    private int unreadCount;
}