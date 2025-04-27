<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page title="Вакансии с откликами | JobSearch" styles="/static/css/vacancies/vacancies.css" activeNav="profile">

    <main class="main">
        <div class="container">
            <h1 class="page-title">Мои вакансии с откликами</h1>

            <div class="filters-container">
                <a href="/profile" class="reset-btn">Вернуться в профиль</a>
            </div>

            <#if vacancies?size == 0>
                <div class="empty-state">
                    <h3>Вакансии с откликами не найдены</h3>
                    <p>У вас нет вакансий с откликами в данный момент.</p>
                </div>
            </#if>

            <div class="vacancies-grid">
                <#list vacancies as vacancy>
                    <div class="vacancy-card">
                        <a href="/vacancies/${vacancy.id}/info" class="vacancy-card-link">
                            <div class="vacancy-header">
                                <h2 class="vacancy-title">${vacancy.name}</h2>
                                <div class="salary">${vacancy.salary?string} c</div>
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
                                <span class="responses">
                                <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z"></path>
                                </svg>
                                Откликов: ${vacancy.responsesCount!0}
                            </span>
                                <span class="update-time">
                                <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                </svg>
                                Обновлена: ${vacancy.updateTime?string("dd.MM.yyyy")}
                            </span>
                            </div>
                        </a>
                        <div class="vacancy-actions">
                            <a href="/response/vacancies/${vacancy.id}" class="company-profile-btn">
                                Кандидаты (${vacancy.responsesCount})
                            </a>
                        </div>
                    </div>
                </#list>
            </div>

            <#if totalPages gt 1>
                <div class="pagination">
                    <#if currentPage gt 0>
                        <a href="/vacancies/${userId}/response?page=0&size=${pageSize}" class="page-link">««</a>
                        <a href="/vacancies/${userId}/response?page=${currentPage - 1}&size=${pageSize}" class="page-link">«</a>
                    </#if>

                    <#list 0..totalPages-1 as i>
                        <#if i == currentPage>
                            <span class="page-link active">${i + 1}</span>
                        <#else>
                            <a href="/vacancies/${userId}/response?page=${i}&size=${pageSize}" class="page-link">${i + 1}</a>
                        </#if>
                    </#list>

                    <#if currentPage lt totalPages - 1>
                        <a href="/vacancies/${userId}/response?page=${currentPage + 1}&size=${pageSize}" class="page-link">»</a>
                        <a href="/vacancies/${userId}/response?page=${totalPages - 1}&size=${pageSize}" class="page-link">»»</a>
                    </#if>

                    <div class="page-size">
                        Элементов на странице:
                        <select onchange="window.location.href='/vacancies/${userId}/response?page=0&size='+this.value" class="page-size-select">
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
