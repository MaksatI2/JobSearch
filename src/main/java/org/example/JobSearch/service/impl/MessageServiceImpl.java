package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.MessageDTO;
import org.example.JobSearch.model.Message;
import org.example.JobSearch.model.RespondedApplicant;
import org.example.JobSearch.repository.MessageRepository;
import org.example.JobSearch.service.MessageService;
import org.example.JobSearch.service.RespondedApplicantProvider;
import org.example.JobSearch.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final RespondedApplicantProvider respondedApplicantService;
    private final UserService userService;

    @Override
    @Transactional
    public MessageDTO saveMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setDescription(messageDTO.getContent());
        message.setSendTime(new Timestamp(Instant.now().toEpochMilli()));
        message.setSenderType(messageDTO.getSenderType());

        RespondedApplicant respondedApplicant = respondedApplicantService.getById(messageDTO.getRespondedApplicantId());

        message.setRespondedApplicant(respondedApplicant);
        message.setRead(false);

        Message savedMessage = messageRepository.save(message);
        messageDTO.setId(savedMessage.getId());
        messageDTO.setSendTime(savedMessage.getSendTime());

        messageDTO.setApplicantId(respondedApplicant.getResume().getApplicant().getId());
        messageDTO.setApplicantFirstName(respondedApplicant.getResume().getApplicant().getName());
        messageDTO.setApplicantLastName(respondedApplicant.getResume().getApplicant().getSurname());
        messageDTO.setApplicantEmail(respondedApplicant.getResume().getApplicant().getEmail());
        messageDTO.setApplicantAvatar("/api/users/" + respondedApplicant.getResume().getApplicant().getId() + "/avatar");

        messageDTO.setEmployerId(respondedApplicant.getVacancy().getAuthor().getId());
        messageDTO.setEmployerCompanyName(respondedApplicant.getVacancy().getAuthor().getName());
        messageDTO.setEmployerEmail(respondedApplicant.getVacancy().getAuthor().getEmail());
        messageDTO.setEmployerAvatar("/api/users/" + respondedApplicant.getVacancy().getAuthor().getId() + "/avatar");

        messageDTO.setVacancyId(respondedApplicant.getVacancy().getId());
        messageDTO.setVacancyTitle(respondedApplicant.getVacancy().getName());

        messageDTO.setResumeId(respondedApplicant.getResume().getId());
        messageDTO.setResumeTitle(respondedApplicant.getResume().getName());

        return messageDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDTO> getMessagesByConversationId(Long respondedApplicantId) {
        List<Message> messages = messageRepository.findByRespondedApplicantIdOrderBySendTimeAsc(respondedApplicantId);

        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getConversationsByApplicantId(Long applicantId, Pageable pageable) {
        List<RespondedApplicant> respondedApplicants = respondedApplicantService.getByApplicantId(applicantId);

        Map<Long, MessageDTO> conversationMap = new HashMap<>();
        for (RespondedApplicant ra : respondedApplicants) {
            List<Message> messages = messageRepository.findByRespondedApplicantIdOrderBySendTimeDesc(ra.getId(), pageable.getPageSize());

            if (!messages.isEmpty()) {
                Message latestMessage = messages.get(0);
                MessageDTO dto = createConversationSummary(ra, latestMessage);

                int unreadCount = messageRepository.countUnreadMessagesInConversation(ra.getId(), false);
                dto.setUnreadCount(unreadCount);

                conversationMap.put(ra.getId(), dto);
            }
        }

        List<MessageDTO> conversations = new ArrayList<>(conversationMap.values());
        conversations.sort((a, b) -> b.getLastMessageTime().compareTo(a.getLastMessageTime()));

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), conversations.size());

        if (start > conversations.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, conversations.size());
        }

        return new PageImpl<>(conversations.subList(start, end), pageable, conversations.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getConversationsByEmployerId(Long employerId, Pageable pageable) {
        List<RespondedApplicant> respondedApplicants = respondedApplicantService.getByEmployerId(employerId);

        Map<Long, MessageDTO> conversationMap = new HashMap<>();

        for (RespondedApplicant ra : respondedApplicants) {
            List<Message> messages = messageRepository.findByRespondedApplicantIdOrderBySendTimeDesc(ra.getId(), pageable.getPageSize());

            if (!messages.isEmpty()) {
                Message latestMessage = messages.get(0);
                MessageDTO dto = createConversationSummary(ra, latestMessage);

                int unreadCount = messageRepository.countUnreadMessagesInConversation(ra.getId(), true);
                dto.setUnreadCount(unreadCount);

                conversationMap.put(ra.getId(), dto);
            }
        }

        List<MessageDTO> conversations = new ArrayList<>(conversationMap.values());
        conversations.sort((a, b) -> b.getLastMessageTime().compareTo(a.getLastMessageTime()));

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), conversations.size());

        if (start > conversations.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, conversations.size());
        }

        return new PageImpl<>(conversations.subList(start, end), pageable, conversations.size());
    }

    private MessageDTO createConversationSummary(RespondedApplicant ra, Message latestMessage) {
        MessageDTO dto = new MessageDTO();
        dto.setRespondedApplicantId(ra.getId());

        dto.setApplicantId(ra.getResume().getApplicant().getId());
        dto.setApplicantFirstName(ra.getResume().getApplicant().getName());
        dto.setApplicantLastName(ra.getResume().getApplicant().getSurname());
        dto.setApplicantEmail(ra.getResume().getApplicant().getEmail());
        dto.setApplicantAvatar("/api/users/" + ra.getResume().getApplicant().getId() + "/avatar");

        dto.setEmployerId(ra.getVacancy().getAuthor().getId());
        dto.setEmployerCompanyName(ra.getVacancy().getAuthor().getName());
        dto.setEmployerEmail(ra.getVacancy().getAuthor().getEmail());
        dto.setEmployerAvatar("/api/users/" + ra.getVacancy().getAuthor().getId() + "/avatar");

        dto.setResumeId(ra.getResume().getId());
        dto.setResumeTitle(ra.getResume().getName());

        dto.setVacancyId(ra.getVacancy().getId());
        dto.setVacancyTitle(ra.getVacancy().getName());

        dto.setLastMessageContent(latestMessage.getDescription());
        dto.setLastMessageTime(latestMessage.getSendTime());

        return dto;
    }

    @Override
    @Transactional
    public void markMessagesAsRead(Long respondedApplicantId, String userEmail) {
        boolean isEmployer = userService.isEmployer(userEmail);

        if (isEmployer) {
            messageRepository.markMessagesAsReadForEmployer(respondedApplicantId);
        } else {
            messageRepository.markMessagesAsReadForApplicant(respondedApplicantId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int countUnreadMessages(String userEmail) {
        boolean isEmployer = userService.isEmployer(userEmail);

        if (isEmployer) {
            return messageRepository.countUnreadMessagesForEmployer(userEmail);
        } else {
            return messageRepository.countUnreadMessagesForApplicant(userEmail);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDTO getConversationDetails(Long respondedApplicantId) {
        RespondedApplicant respondedApplicant = respondedApplicantService.getById(respondedApplicantId);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setRespondedApplicantId(respondedApplicantId);

        messageDTO.setApplicantId(respondedApplicant.getResume().getApplicant().getId());
        messageDTO.setApplicantFirstName(respondedApplicant.getResume().getApplicant().getName());
        messageDTO.setApplicantLastName(respondedApplicant.getResume().getApplicant().getSurname());
        messageDTO.setApplicantEmail(respondedApplicant.getResume().getApplicant().getEmail());
        messageDTO.setApplicantAvatar("/api/users/" + respondedApplicant.getResume().getApplicant().getId() + "/avatar");

        messageDTO.setEmployerId(respondedApplicant.getVacancy().getAuthor().getId());
        messageDTO.setEmployerCompanyName(respondedApplicant.getVacancy().getAuthor().getName());
        messageDTO.setEmployerEmail(respondedApplicant.getVacancy().getAuthor().getEmail());
        messageDTO.setEmployerAvatar("/api/users/" + respondedApplicant.getVacancy().getAuthor().getId() + "/avatar");

        messageDTO.setResumeId(respondedApplicant.getResume().getId());
        messageDTO.setResumeTitle(respondedApplicant.getResume().getName());

        messageDTO.setVacancyId(respondedApplicant.getVacancy().getId());
        messageDTO.setVacancyTitle(respondedApplicant.getVacancy().getName());

        return messageDTO;
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getDescription());
        dto.setSendTime(message.getSendTime());
        dto.setSenderType(message.getSenderType());
        dto.setRead(message.isRead());
        dto.setRespondedApplicantId(message.getRespondedApplicant().getId());

        return dto;
    }
}