<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/vacancies/vacancies.css" activeNav="favorite">
    <h1 class="page-title"><@spring.message code="vacancies.favorites.title"/></h1>

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
        <a href="/vacancies" class="submit-btn">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" class="btn-icon">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
            </svg>
            <@spring.message code="vacancies.favorites.back.vacancies"/>
        </a>
    </div>

    <#if vacanciesPage?? && vacanciesPage.content?size == 0>
        <div class="empty-state">
            <h3><@spring.message code="vacancies.favorites.no.vacancies"/></h3>
            <p><@spring.message code="vacancies.favorites.empty.vacancies"/></p>
            <a href="/vacancies" class="submit-btn"><@spring.message code="vacancies.favorites.show"/></a>
        </div>
    <#else>
        <div class="vacancies-grid">
            <#list vacanciesPage.content as vacancy>
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
                                <@spring.message code="vacancies.card.exp"/> ${vacancy.expFrom}-${vacancy.expTo}
                                <@spring.message code="vacancies.card.exp.years"/>
                            </span>
                            </span>
                            <span class="author">
                                <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                                </svg>
                                <@spring.message code="vacancies.card.author"/> <span>${vacancy.authorName}</span>
                            </span>
                            <span class="update-time">
                                <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                </svg>
                                <@spring.message code="vacancies.card.date"/> ${vacancy.updateTime?string("dd.MM.yyyy")}
                            </span>
                        </div>
                    </a>
                    <div class="favorite-actions">
                        <form action="/favorites/vacancies/${vacancy.id}/remove" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="remove-favorite-btn">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                                </svg>
                                <@spring.message code="favorites.delete"/>
                            </button>
                        </form>
                    </div>
                </div>
            </#list>
        </div>

        <#if totalPages gt 1>
            <div class="pagination">
                <#if currentPage gt 0>
                    <a href="/favorites/vacancies?page=0&size=${pageSize}" class="page-link">««</a>
                    <a href="/favorites/vacancies?page=${currentPage - 1}&size=${pageSize}" class="page-link">«</a>
                </#if>

                <#list 0..totalPages-1 as i>
                    <#if i == currentPage>
                        <span class="page-link active">${i + 1}</span>
                    <#else>
                        <a href="/favorites/vacancies?page=${i}&size=${pageSize}" class="page-link">${i + 1}</a>
                    </#if>
                </#list>

                <#if currentPage lt totalPages - 1>
                    <a href="/favorites/vacancies?page=${currentPage + 1}&size=${pageSize}" class="page-link">»</a>
                    <a href="/favorites/vacancies?page=${totalPages - 1}&size=${pageSize}" class="page-link">»»</a>
                </#if>

                <div class="page-size">
                    <@spring.message code="pagination"/>
                    <select onchange="window.location.href='/favorites/vacancies?page=0&size='+this.value" class="page-size-select">
                        <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                        <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                        <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                        <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                    </select>
                </div>
            </div>
        </#if>
    </#if>
</@layout.page>