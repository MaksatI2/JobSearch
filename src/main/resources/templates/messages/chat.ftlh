<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>
<@layout.page title="JobSearch - Чат" styles="/static/css/chat/chat.css" activeNav="messages">
<div class="chat-container">
    <div class="chat-sidebar">
        <div class="profile-card">
            <div class="avatar-container mb-3">
                <img src="${isEmployer?then(conversation.applicantAvatar, conversation.employerAvatar)}"
                     alt="${isEmployer?then(conversation.applicantFirstName, conversation.employerCompanyName)}"
                     class="avatar-img">
            </div>
            <div class="profile-info">
                <h3>${isEmployer?then(conversation.applicantFirstName + ' ' + conversation.applicantLastName, conversation.employerCompanyName)}</h3>
                <p class="email">${isEmployer?then(conversation.applicantEmail, conversation.employerEmail)}</p>
            </div>

            <hr>

            <div class="related-items">
                <div class="related-item">
                    <h4><@spring.message code="messages.vacancy"/></h4>
                    <a href="/vacancies/${conversation.vacancyId}/info" class="badge bg-light text-dark p-2 vacancy-badge">
                        ${conversation.vacancyTitle}
                    </a>
                </div>

                <div class="related-item mt-3">
                    <h4><@spring.message code="messages.resume" /></h4>
                    <a href="/resumes/${conversation.resumeId}/info" class="badge bg-light text-dark p-2 resume-badge">
                        ${conversation.resumeTitle}
                    </a>
                </div>
            </div>

            <div class="back-to-list mt-4">
                <a href="/messages" class="btn btn-outline-primary btn-sm">
                    <i class="bi bi-arrow-left"></i> <@spring.message code="chat.back" />
                </a>
            </div>
        </div>
    </div>

    <div class="chat-area">
        <div class="chat-header">
            <h2>
                <@spring.message code="chat.with"/>
                ${isEmployer?then(conversation.applicantFirstName + ' ' + conversation.applicantLastName, conversation.employerCompanyName)}
            </h2>
        </div>

        <div class="message-list" id="messageList">
            <#if messages?? && messages?size gt 0>
                <#list messages as message>
                    <#assign senderTypeToCompare = isEmployer?then('EMPLOYER', 'APPLICANT')>
                    <div class="message ${(message.senderType == senderTypeToCompare)?then('message-sent', 'message-received')}">
                        <div class="message-content">
                            <p>${message.content}</p>
                            <span class="message-time">${message.sendTime?string("HH:mm")}</span>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="empty-chat">
                    <div class="empty-chat-content">
                        <h3><@spring.message code="chat.start"/></h3>
                        <p><@spring.message code="chat.first.message"/></p>
                    </div>
                </div>
            </#if>
        </div>

        <div class="message-input">
            <form id="messageForm" class="message-form">
                <input type="hidden" id="respondedApplicantId" value="${conversation.respondedApplicantId}">
                <div class="input-group">
                    <textarea class="form-control" id="messageContent" placeholder="<@spring.message code="chat.write.message"/>" required></textarea>
                    <button type="submit" class="btn btn-primary send-btn">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M22 2L11 13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M22 2L15 22L11 13L2 9L22 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stomp-websocket@2.3.4-next/lib/stomp.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const messageList = document.getElementById('messageList');
        const messageForm = document.getElementById('messageForm');
        const messageContent = document.getElementById('messageContent');
        const respondedApplicantId = document.getElementById('respondedApplicantId').value;

        function scrollToBottom() {
            messageList.scrollTop = messageList.scrollHeight;
        }
        scrollToBottom();
        const socket = new SockJS('/ws');
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);

            stompClient.subscribe('/topic/chat.' + respondedApplicantId, function(response) {
                const message = JSON.parse(response.body);
                displayMessage(message);
                scrollToBottom();
            });
        });

        messageForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const content = messageContent.value.trim();
            if (content) {
                const message = {
                    respondedApplicantId: respondedApplicantId,
                    content: content
                };

                stompClient.send("/app/chat.send", {}, JSON.stringify(message));
                messageContent.value = '';
            }
        });
        function displayMessage(message) {
            const isEmployer = ${isEmployer?string};
            const isSender = (isEmployer && message.senderType === 'EMPLOYER') ||
                (!isEmployer && message.senderType === 'APPLICANT');

            const messageDiv = document.createElement('div');
            messageDiv.className = 'message ' + (isSender ? 'message-sent' : 'message-received');

            const messageTime = new Date(message.sendTime);
            const formattedTime = messageTime.getHours().toString().padStart(2, '0') + ':' +
                messageTime.getMinutes().toString().padStart(2, '0');

            messageDiv.innerHTML = '<div class="message-content">' +
                '<p>' + message.content + '</p>' +
                '<span class="message-time">' + formattedTime + '</span>' +
                '</div>';

            const emptyChat = document.querySelector('.empty-chat');
            if (emptyChat) {
                emptyChat.remove();
            }

            messageList.appendChild(messageDiv);
        }

        messageContent.addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = (this.scrollHeight) + 'px';
        });
    });
</script>
</@layout.page>