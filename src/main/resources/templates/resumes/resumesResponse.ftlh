<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page title="Резюме с откликами | JobSearch" styles="/static/css/resumes/resumes.css" activeNav="profile">

    <main class="main">
        <div class="container">
            <h1 class="page-title">Мои резюме с откликами</h1>

            <div class="filters-container">
                <a href="/profile" class="reset-btn">Вернуться в профиль</a>
            </div>

            <#if resumes?size == 0>
                <div class="empty-state">
                    <h3>Резюме с откликами не найдены</h3>
                    <p>У вас нет резюме с откликами в данный момент.</p>
                </div>
            </#if>

            <div class="resumes-grid">
                <#list resumes as resume>
                    <div class="resume-card">
                        <a href="/resumes/${resume.id}/info" class="resume-card-link">
                            <div class="resume-header">
                                <h2 class="resume-title">${resume.name}</h2>
                                <div class="salary">${resume.salary?string} c</div>
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
                                <span class="responses">
                                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z"></path>
                                    </svg>
                                    Откликов: ${resume.responsesCount!0}
                                </span>
                                <span class="update-time">
                                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                    </svg>
                                    Обновлено: ${resume.updateTime?string("dd.MM.yyyy")}
                                </span>
                            </div>
                        </a>
                        <div class="resume-actions">
                            <a href="#" class="submit-btn">
                                Вакансии (${resume.responsesCount})
                            </a>
                        </div>
                    </div>
                </#list>
            </div>

            <#if totalPages gt 1>
                <div class="pagination">
                    <#if currentPage gt 0>
                        <a href="/resumes/${userId}/response?page=0&size=${pageSize}" class="page-link">««</a>
                        <a href="/resumes/${userId}/response?page=${currentPage - 1}&size=${pageSize}" class="page-link">«</a>
                    </#if>

                    <#list 0..totalPages-1 as i>
                        <#if i == currentPage>
                            <span class="page-link active">${i + 1}</span>
                        <#else>
                            <a href="/resumes/${userId}/response?page=${i}&size=${pageSize}" class="page-link">${i + 1}</a>
                        </#if>
                    </#list>

                    <#if currentPage lt totalPages - 1>
                        <a href="/resumes/${userId}/response?page=${currentPage + 1}&size=${pageSize}" class="page-link">»</a>
                        <a href="/resumes/${userId}/response?page=${totalPages - 1}&size=${pageSize}" class="page-link">»»</a>
                    </#if>

                    <div class="page-size">
                        Элементов на странице:
                        <select onchange="window.location.href='/resumes/${userId}/response?page=0&size='+this.value" class="page-size-select">
                            <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                            <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                            <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                            <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                        </select>
                    </div>
                </div>
            </#if>
        </div>
    </main>

</@layout.page>