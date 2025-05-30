<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout >

<@layout.page title="JobSearch - Сообщения" styles="/static/css/chat/chat-list.css" activeNav="messages">
<div class="chat-list-container">
    <h1 class="mb-4"><@spring.message code="messages.title" /></h1>
    <div class="card chat-list-card">
        <div class="card-body p-0">
            <div class="list-group chat-list">
                <#if chats?? && chats?size gt 0>
                    <#list chats as chat>
                        <a href="/messages/${chat.respondedApplicantId}" class="list-group-item list-group-item-action chat-item">
                            <div class="d-flex align-items-center">
                                <div class="chat-avatar">
                                    <img src="${isEmployer?then(chat.applicantAvatar, chat.employerAvatar)}"
                                         alt="${isEmployer?then(chat.applicantFirstName, chat.employerCompanyName)}"
                                         class="avatar-img">
                                </div>
                                <div class="chat-info flex-grow-1 ms-3">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h5 class="mb-1">
                                            ${isEmployer?then(chat.applicantFirstName + ' ' + chat.applicantLastName, chat.employerCompanyName)}
                                        </h5>
                                        <small class="text-muted">${chat.lastMessageTime?string("dd.MM.yyyy HH:mm")}</small>
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <p class="mb-1 text-truncate chat-preview">${chat.lastMessageContent}</p>
                                        <#if chat.unreadCount gt 0>
                                            <span class="badge bg-primary rounded-pill">${chat.unreadCount}</span>
                                        </#if>
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center mt-1">
                                        <small class="text-muted">
                                            <span class="vacancy-label"><@spring.message code="messages.vacancy" />:</span>
                                            ${chat.vacancyTitle}
                                        </small>
                                        <small class="text-muted">
                                            <span class="resume-label"><@spring.message code="messages.resume" />:</span>
                                            ${chat.resumeTitle}
                                        </small>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </#list>
                <#else>
                    <div class="empty-chat-list p-4 text-center">
                        <h3><@spring.message code="messages.empty"/></h3>
                        <p class="text-muted">
                            <#if isEmployer>
                                <@spring.message code="messages.empty.employer"/>
                            <#else>
                                <@spring.message code="messages.empty.applicant"/>
                            </#if>
                        </p>
                        <a href="${isEmployer?then('/resumes/allResumes', '/vacancies')}" class="btn btn-primary">
                            <#if isEmployer>
                                <@spring.message code="messages.view.resumes" />
                            <#else>
                                <@spring.message code="messages.view.vacancies" />
                            </#if>
                        </a>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</div>

<script>
    var socket = new SockJS('/ws');
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/user/queue/messages', function(notification) {
            updateUnreadCount();

            var audio = new Audio('/static/sounds/notification.mp3');
            audio.play();
        });

        updateUnreadCount();
        setInterval(updateUnreadCount, 30000);
    });

    function updateUnreadCount() {
        fetch('/messages/unread-count')
            .then(response => response.json())
            .then(count => {
                const unreadBadge = document.getElementById('unread-badge');
                if (unreadBadge) {
                    unreadBadge.textContent = count;
                    unreadBadge.style.display = count > 0 ? 'inline-block' : 'none';
                }
            });
    }
</script>
</@layout.page>