<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page
title="Список вакансий | JobSearch"
styles="/static/css/vacancies/vacancies.css"
activeNav="vacancies">

    <h1 class="page-title"><@spring.messageText "vacancies.title", "Все вакансии"/></h1>
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
        <form method="get" action="/vacancies" class="filter-form">
            <input type="hidden" name="page" value="0">
            <input type="hidden" name="size" value="${pageSize}">

            <div class="filter-group">
                <label for="sort">Сортировка:</label>
                <select id="sort" name="sort" class="form-control">
                    <option value="">По дате (новые)</option>
                    <option value="salaryAsc" <#if selectedSort?? && selectedSort == "salaryAsc">selected</#if>>
                        По зарплате (↑)
                    </option>
                    <option value="salaryDesc" <#if selectedSort?? && selectedSort == "salaryDesc">selected</#if>>
                        По зарплате (↓)
                    </option>
                    <option value="expAsc" <#if selectedSort?? && selectedSort == "expAsc">selected</#if>>
                        По опыту (↑)
                    </option>
                    <option value="expDesc" <#if selectedSort?? && selectedSort == "expDesc">selected</#if>>
                        По опыту (↓)
                    </option>
                    <option value="responsesDesc" <#if selectedSort?? && selectedSort == "responsesDesc">selected</#if>>
                        По количеству откликов (↓)
                    </option>
                </select>
            </div>

            <div class="filter-actions">
                <button type="submit" class="submit-btn">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" class="btn-icon">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                    </svg>
                    Применить
                </button>
                <#if selectedSort??>
                    <a href="/vacancies?page=0&size=${pageSize}" class="reset-btn">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" class="btn-icon">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
                        </svg>
                        Сбросить
                    </a>
                </#if>
            </div>
        </form>
    </div>

    <#if vacancies?size == 0>
        <div class="empty-state">
            <h3>Вакансии не найдены</h3>
            <p>В данный момент нет активных вакансий. Попробуйте позже.</p>
        </div>
    </#if>

    <div class="vacancies-grid">
        <#list vacancies as vacancy>
            <div class="vacancy-card">
                <a href="/vacancies/${vacancy.id}/info" class="vacancy-card-link">
                    <div class="vacancy-header">
                        <h2 class="vacancy-title">${vacancy.name}</h2>
                        <div class="salary">${vacancy.salary} с</div>
                    </div>
                    <p class="vacancy-description">${vacancy.description}</p>
                    <div class="vacancy-meta">
                        <span class="experience">
                            <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                            Опыт: ${vacancy.expFrom}-${vacancy.expTo} лет
                        </span>
                        <span class="author">
                            <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                            </svg>
                            Автор: ${vacancy.authorName!""}
                        </span>
                        <span class="update-time">
                            <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                            </svg>
                            Обновлена: ${vacancy.updateTime?string("dd.MM.yyyy")}
                        </span>

                        <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                            <form action="/favorites/vacancies/${vacancy.id}/add" method="post" class="favorite-form">
                                <input type="hidden" name="vacancyId" value="${vacancy.id}">
                                <#if _csrf??>
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                </#if>
                                <button type="submit" class="favorite-btn" title="Добавить в избранное">
                                    <svg width="24" height="24" viewBox="0 0 24 24"
                                         fill="<#if favoriteVacancyIds?? && favoriteVacancyIds?seq_contains(vacancy.id)>gold<#else>none</#if>"
                                         stroke="currentColor" stroke-width="2"
                                         stroke-linecap="round" stroke-linejoin="round" class="favorite-icon">
                                        <path d="M5 3v18l7-5 7 5V3z"/>
                                    </svg>
                                </button>
                            </form>
                        </#if>
                    </div>
                </a>
                <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <#if vacancy.authorId??>
                        <a href="/profile/viewEmployer/${vacancy.authorId}" class="company-profile-btn">
                            Профиль компании
                        </a>
                    </#if>
                    <a href="#modal-${vacancy.id}" class="respond-btn">Откликнуться</a>
                </#if>
            </div>

            <div id="modal-${vacancy.id}" class="modal">
                <div class="modal-content">
                    <a href="#" class="close-modal">&times;</a>
                    <h3 class="modal-title">Отклик на вакансию "${vacancy.name}"</h3>

                    <form action="/response/respond" method="post">
                        <input type="hidden" name="vacancyId" value="${vacancy.id}">
                        <#if _csrf??>
                            <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>

                        <label for="resumeId-${vacancy.id}">Выберите резюме:</label>
                        <select id="resumeId-${vacancy.id}" name="resumeId" class="resume-select" required>
                            <#if userResumes?? && userResumes?size gt 0>
                                <#list userResumes as resume>
                                    <option value="${resume.id}">${resume.name} (${resume.updateTime?string("dd.MM.yyyy")})</option>
                                </#list>
                            <#else>
                                <option value="" disabled>У вас нет резюме</option>
                            </#if>
                        </select>

                        <button type="submit" class="submit-resume-btn">Отправить отклик</button>
                    </form>
                </div>
            </div>
        </#list>
    </div>

    <#if totalPages gt 1>
        <div class="pagination">
            <#if currentPage gt 0>
                <a href="/vacancies?page=0&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                   class="page-link">««</a>
                <a href="/vacancies?page=${currentPage - 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                   class="page-link">«</a>
            </#if>

            <#list 0..totalPages-1 as i>
                <#if i == currentPage>
                    <span class="page-link active">${i + 1}</span>
                <#else>
                    <a href="/vacancies?page=${i}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                       class="page-link">${i + 1}</a>
                </#if>
            </#list>

            <#if currentPage lt totalPages - 1>
                <a href="/vacancies?page=${currentPage + 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                   class="page-link">»</a>
                <a href="/vacancies?page=${totalPages - 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                   class="page-link">»»</a>
            </#if>

            <div class="page-size">
                Элементов на странице:
                <select onchange="window.location.href='/vacancies?page=0&size='+this.value+'<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>'"
                        class="page-size-select">
                    <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                    <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                    <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                    <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                </select>
            </div>
        </div>
    </#if>
</@layout.page>