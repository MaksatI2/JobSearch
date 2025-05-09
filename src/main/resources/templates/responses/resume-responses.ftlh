<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/vacancies/vacancies.css" activeNav="profile">
    <a href="/profile" class="reset-btn"><@spring.message code="response.back"/></a>
    <div class="responses-container">
        <h1 class="page-title"><@spring.message code="response.resume.title"/></h1>

        <div class="responses-header">
            <h2><@spring.message code="response.resume"/> ${resume.name!''}</h2>
            <p class="vacancy-title"><@spring.message code="response.resume.count"/> ${totalResponses}</p>
        </div>

        <#if vacancies.content?has_content>
            <div class="vacancies-grid">
                <#list vacancies.content as vacancy>
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
                                <span class="author">
                            <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                            </svg>
                            <@spring.message code="vacancies.card.author"/> ${vacancy.authorName!""}
                        </span>
                                <span class="update-time">
                            <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                            </svg>
                            <@spring.message code="vacancies.card.date"/> ${vacancy.updateTime?string("dd.MM.yyyy")}
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
                                    <@spring.message code="vacancies.company.profile"/>
                                </a>
                            </#if>
                        </#if>
                    </div>
                </#list>
            </div>

            <div class="pagination-wrapper">
                <#if vacancies.totalPages gt 1>
                    <div class="pagination">
                        <#if vacancies.number gt 0>
                            <a href="/response/resumes/${resume.id}?page=0&size=${pageSize}" class="page-link">««</a>
                            <a href="/response/resumes/${resume.id}?page=${vacancies.number - 1}&size=${pageSize}" class="page-link">«</a>
                        </#if>
                        <#list 1..vacancies.totalPages as i>
                            <#if i == vacancies.number + 1>
                                <span class="page-link active">${i}</span>
                            <#else>
                                <a href="/response/resumes/${resume.id}?page=${i - 1}&size=${pageSize}" class="page-link">${i}</a>
                            </#if>
                        </#list>
                        <#if vacancies.number lt vacancies.totalPages - 1>
                            <a href="/response/resumes/${resume.id}?page=${vacancies.number + 1}&size=${pageSize}" class="page-link">»</a>
                            <a href="/response/resumes/${resume.id}?page=${vacancies.totalPages - 1}&size=${pageSize}" class="page-link">»»</a>
                        </#if>
                        <div class="page-size">
                            <@spring.message code="pagination"/>
                            <select onchange="window.location.href='/response/resumes/${resume.id}?page=0&size='+this.value" class="page-size-select">
                                <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                                <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                                <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                                <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                            </select>
                        </div>
                    </div>
                </#if>
            </div>
        <#else>
            <div class="no-responses">
                <p><@spring.message code="response.resume.no.response"/></p>
                <a href="/vacancies" class="find-vacancies-btn"><@spring.message code="vacancies.favorites.show"/></a>
            </div>
        </#if>
    </div>
</@layout.page>
