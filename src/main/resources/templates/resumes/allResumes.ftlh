<#import "../layout.ftlh" as layout>

<@layout.page title="Активные резюме | JobSearch" styles="/static/css/resumes/resumes.css" activeNav="resumes">

    <div class="container">
        <h1 class="page-title">Список активных резюме</h1>

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

        <div class="filters-container">
            <form method="get" action="/resumes/allResumes" class="filter-form">
                <input type="hidden" name="page" value="0">
                <input type="hidden" name="size" value="${pageSize}">

                <div class="filter-group">
                    <label for="sort">Сортировка:</label>
                    <select id="sort" name="sort" class="form-control">
                        <option value="">По дате (новые)</option>
                        <option value="salaryAsc" <#if selectedSort?? && selectedSort == "salaryAsc">selected</#if>>По зарплате (↑)</option>
                        <option value="salaryDesc" <#if selectedSort?? && selectedSort == "salaryDesc">selected</#if>>По зарплате (↓)</option>
                    </select>
                </div>

                <button type="submit" class="submit-btn">Применить</button>

                <#if selectedSort??>
                    <a href="/vacancies?page=0&size=${pageSize}" class="reset-btn">Сбросить</a>
                </#if>
            </form>
        </div>

        <#if resumes?? && resumes?has_content>
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

                            <form action="/favorites/resumes/${resume.id}/add" method="post" class="favorite-form">
                                <input type="hidden" name="resumeId" value="${resume.id}">
                                <#if _csrf??>
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                </#if>
                                <button type="submit" class="favorite-btn" title="Добавить в избранное">
                                    <svg width="24" height="24" viewBox="0 0 24 24"
                                         fill="<#if favoriteResumeIds?? && favoriteResumeIds?seq_contains(resume.id)>gold<#else>none</#if>"
                                         stroke="currentColor" stroke-width="2"
                                         stroke-linecap="round" stroke-linejoin="round" class="favorite-icon">
                                        <path d="M5 3v18l7-5 7 5V3z"/>
                                    </svg>
                                </button>
                            </form>

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

            <#if totalPages gt 1>
                <div class="pagination">
                    <#if currentPage gt 0>
                        <a href="/resumes/allResumes?page=0&size=${pageSize}" class="page-link">««</a>
                        <a href="/resumes/allResumes?page=${currentPage - 1}&size=${pageSize}" class="page-link">«</a>
                    </#if>

                    <#list 0..totalPages-1 as i>
                        <#if i == currentPage>
                            <span class="page-link active">${i + 1}</span>
                        <#else>
                            <a href="/resumes/allResumes?page=${i}&size=${pageSize}" class="page-link">${i + 1}</a>
                        </#if>
                    </#list>

                    <#if currentPage lt totalPages - 1>
                        <a href="/resumes/allResumes?page=${currentPage + 1}&size=${pageSize}" class="page-link">»</a>
                        <a href="/resumes/allResumes?page=${totalPages - 1}&size=${pageSize}" class="page-link">»»</a>
                    </#if>

                    <div class="page-size">
                        Элементов на странице:
                        <select onchange="window.location.href='/resumes/allResumes?page=0&size='+this.value" class="page-size-select">
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
                <h3>Резюме не найдены</h3>
                <p>Попробуйте зайти позже</p>
            </div>
        </#if>
    </div>

</@layout.page>
