package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.MessageDTO;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.service.MessageService;
import org.example.JobSearch.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private static final int CHATS_PER_PAGE = 10;

    @GetMapping
    public String showChatList(Principal principal, Model model,
                               @RequestParam(defaultValue = "0") int page) {
        boolean isEmployer = userService.isEmployer(principal.getName());
        Long userId = userService.getUserId(principal.getName());

        PageRequest pageRequest = PageRequest.of(page, CHATS_PER_PAGE,
                Sort.by("lastMessageTime").descending());

        Page<MessageDTO> chatsPage;
        if (isEmployer) {
            chatsPage = messageService.getConversationsByEmployerId(userId, pageRequest);
        } else {
            chatsPage = messageService.getConversationsByApplicantId(userId, pageRequest);
        }

        model.addAttribute("chats", chatsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", chatsPage.getTotalPages());
        model.addAttribute("isEmployer", isEmployer);
        model.addAttribute("activeNav", "messages");

        return "messages/chat-list";
    }

    @GetMapping("/{respondedApplicantId}")
    public String showChat(@PathVariable Long respondedApplicantId, Principal principal, Model model) {
        boolean isEmployer = userService.isEmployer(principal.getName());

        messageService.markMessagesAsRead(respondedApplicantId, principal.getName());
        MessageDTO conversationDetails = messageService.getConversationDetails(respondedApplicantId);

        List<MessageDTO> messages = messageService.getMessagesByConversationId(respondedApplicantId);

        model.addAttribute("conversation", conversationDetails);
        model.addAttribute("messages", messages);
        model.addAttribute("isEmployer", isEmployer);
        model.addAttribute("currentUserEmail", principal.getName());
        model.addAttribute("activeNav", "messages");

        return "messages/chat";
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MessageDTO messageDTO, Principal principal) {
        boolean isEmployer = userService.isEmployer(principal.getName());

        messageDTO.setSenderType(isEmployer ? AccountType.EMPLOYER : AccountType.APPLICANT);

        MessageDTO savedMessage = messageService.saveMessage(messageDTO);

        messagingTemplate.convertAndSend(
                "/topic/chat." + messageDTO.getRespondedApplicantId(),
                savedMessage
        );
        String recipientEmail = isEmployer ? savedMessage.getApplicantEmail() : savedMessage.getEmployerEmail();
        messagingTemplate.convertAndSendToUser(
                recipientEmail,
                "/queue/messages",
                savedMessage
        );
    }

    @GetMapping("/unread-count")
    @ResponseBody
    public int getUnreadCount(Principal principal) {
        return messageService.countUnreadMessages(principal.getName());
    }
}