package org.example.JobSearch.service;

import org.example.JobSearch.dto.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    MessageDTO saveMessage(MessageDTO messageDTO);

    List<MessageDTO> getMessagesByConversationId(Long respondedApplicantId);

    Page<MessageDTO> getConversationsByApplicantId(Long applicantId, Pageable pageable);

    Page<MessageDTO> getConversationsByEmployerId(Long employerId, Pageable pageable);

    void markMessagesAsRead(Long respondedApplicantId, String userEmail);

    int countUnreadMessages(String userEmail);

    MessageDTO getConversationDetails(Long respondedApplicantId);
}