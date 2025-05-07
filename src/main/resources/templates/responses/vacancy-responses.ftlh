<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/resumes/resumes.css" activeNav="profile">
    <a href="/profile" class="reset-btn">← Назад к профилю</a>
    <#if resumes.content?? && resumes.content?has_content>
        <div class="resumes-grid">
            <#list resumes.content as resume>
                <a href="/resumes/${resume.id}/info" class="resume-link">
                    <article class="resume-card">
                        <div class="resume-header">
                            <h2 class="resume-title">${resume.name}</h2>
                            <span class="salary">${resume.salary} с</span>
                        </div>
                        <div class="resume-author">
                            <#if resume.applicantAvatar?? && resume.applicantAvatar?has_content>
                                <img src="${resume.applicantAvatar}" alt="Аватар" class="avatar-small">
                            <#else>
                                <svg class="icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                                </svg>
                            </#if>
                            <span>${resume.applicantName!'Неизвестно'}</span>
                            <#if resume.applicantAge??>
                                <span class="age">${resume.applicantAge} лет</span>
                            </#if>
                        </div>
                        <div class="resume-meta">
                            <span class="update-time">
                                <svg class="icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                </svg>
                                ${resume.updateTime?string("dd.MM.yyyy")}
                            </span>
                        </div>
                        <a href="/profile/viewApplicant/${resume.applicantId}" class="applicant-profile-btn">
                            Профиль соискателя
                        </a>
                    </article>
                </a>
            </#list>
        </div>
        <#if resumes.totalPages gt 1>
            <div class="pagination">
                <#if resumes.number gt 0>
                    <a href="/response/vacancies/${vacancy.id}?page=0&size=${pageSize}" class="page-link">««</a>
                    <a href="/response/vacancies/${vacancy.id}?page=${resumes.number - 1}&size=${pageSize}" class="page-link">«</a>
                </#if>
                <#list 1..resumes.totalPages as i>
                    <#if i == resumes.number + 1>
                        <span class="page-link active">${i}</span>
                    <#else>
                        <a href="/response/vacancies/${vacancy.id}?page=${i - 1}&size=${pageSize}" class="page-link">${i}</a>
                    </#if>
                </#list>
                <#if resumes.number lt resumes.totalPages - 1>
                    <a href="/response/vacancies/${vacancy.id}?page=${resumes.number + 1}&size=${pageSize}" class="page-link">»</a>
                    <a href="/response/vacancies/${vacancy.id}?page=${resumes.totalPages - 1}&size=${pageSize}" class="page-link">»»</a>
                </#if>
                <div class="page-size">
                    Элементов на странице:
                    <select onchange="window.location.href='/response/vacancies/${vacancy.id}?page=0&size='+this.value" class="page-size-select">
                        <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                        <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                        <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                        <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                    </select>
                </div>
            </div>
        </#if>
    <#else>
        <div class="empty-state">
            <h3>На эту вакансию пока нет откликов</h3>
            <p>Попробуйте зайти позже</p>
        </div>
    </#if>
</@layout.page>