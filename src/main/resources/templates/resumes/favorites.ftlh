<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/resumes/resumes.css" activeNav="favorite">

            <h1 class="page-title">Избранные резюме</h1>

            <#if successMessage??>
                <div class="alert alert-success">
                    ${successMessage}
                </div>
            </#if>

            <#if errorMessage??>
                <div class="alert alert-danger">
                    ${errorMessage}
                </div>
            </#if>

            <div class="favorite-container">
                <a href="/resumes/allResumes" class="submit-btn">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" class="btn-icon">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                    </svg>
                    Вернуться к резюме
                </a>
            </div>

            <#if resumes?size == 0>
                <div class="empty-state">
                    <h3>Нет избранных резюме</h3>
                    <p>Вы еще не добавили ни одного резюме в избранное.</p>
                    <a href="/resumes/allResumes" class="submit-btn">Посмотреть резюме</a>
                </div>
            <#else>
                <div class="resumes-grid">
                    <#list resumes as resume>
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

                                <div class="favorite-actions">
                                    <form action="/favorites/resumes/${resume.id}/remove" method="post">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <button type="submit" class="remove-favorite-btn">
                                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                      d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                                            </svg>
                                            Удалить из избранного
                                        </button>
                                    </form>
                                </div>

                                <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('APPLICANT')>
                                    <#if resume.applicantId??>
                                        <a href="/profile/viewApplicant/${resume.applicantId}" class="applicant-profile-btn">
                                            Профиль соискателя
                                        </a>
                                    </#if>
                                </#if>
                            </article>
                        </a>
                    </#list>
                </div>
            </#if>

</@layout.page>
