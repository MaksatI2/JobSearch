<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/vacancies/vacancies.css" activeNav="profile">
    <div class="container py-4">
        <div class="mb-4">
            <a href="/profile" class="btn btn-outline-secondary">
                <i class="bi bi-arrow-left me-1"></i>
                <@spring.message code="response.back"/>
            </a>
        </div>

        <div class="card border-0 shadow-sm mb-4">
            <div class="card-body p-4">
                <h1 class="profile-title mb-2"><@spring.message code="response.resume.title"/></h1>
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="mb-0"><@spring.message code="response.resume"/> ${resume.name!''}</h5>
                    <span class="badge bg-primary"><@spring.message code="response.resume.count"/> ${totalResponses}</span>
                </div>
            </div>
        </div>

        <#if vacancies.content?has_content>
            <div class="row g-4">
                <#list vacancies.content as vacancy>
                    <div class="col-lg-6">
                        <a href="/vacancies/${vacancy.id}/info" class="text-decoration-none">
                        <div class="card vacancy-card h-100 border-0 shadow-sm">
                            <div class="card-body p-4 position-relative">
                                <div class="d-flex justify-content-between align-items-start mb-3">
                                        <h5 class="card-title text-primary">${vacancy.name}</h5>
                                    <span class="badge bg-primary rounded-pill fs-5">${vacancy.salary} с</span>
                                </div>

                                <p class="text-muted mb-3">${vacancy.description}</p>

                                <div class="d-flex flex-wrap gap-3 text-muted mb-3">
                                    <div class="d-flex align-items-center">
                                        <i class="bi bi-clock-history me-1"></i>
                                        <small><@spring.message code="vacancies.card.exp"/> ${vacancy.expFrom}-${vacancy.expTo}
                                            <@spring.message code="vacancies.card.exp.years"/></small>
                                    </div>
                                    <div class="d-flex align-items-center">
                                        <i class="bi bi-person me-1"></i>
                                        <small><@spring.message code="vacancies.card.author"/> ${vacancy.authorName!""}</small>
                                    </div>
                                    <div class="d-flex align-items-center">
                                        <i class="bi bi-calendar me-1"></i>
                                        <small><@spring.message code="vacancies.card.date"/> ${vacancy.updateTime?string("dd.MM.yyyy")}</small>
                                    </div>
                                </div>

                                <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                                    <form action="/favorites/vacancies/${vacancy.id}/add" method="post" class="position-absolute top-0 end-0 mt-2 me-2">
                                        <input type="hidden" name="vacancyId" value="${vacancy.id}">
                                        <#if _csrf??>
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        </#if>
                                        <button type="submit" class="btn btn-link text-warning p-0 border-0" title="Добавить в избранное">
                                            <i class="bi bi-bookmark-star<#if favoriteVacancyIds?? && favoriteVacancyIds?seq_contains(vacancy.id)>-fill</#if> fs-4"></i>
                                        </button>
                                    </form>
                                </#if>

                                <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                                    <#if vacancy.authorId??>
                                        <a href="/profile/viewEmployer/${vacancy.authorId}" class="btn btn-primary mt-2">
                                            <@spring.message code="vacancies.company.profile"/>
                                        </a>
                                    </#if>
                                </#if>
                            </div>
                        </div>
                        </a>
                    </div>
                </#list>
            </div>

            <#if vacancies.totalPages gt 1>
                <nav aria-label="Page navigation" class="mt-4">
                    <ul class="pagination justify-content-center">
                        <#if vacancies.number gt 0>
                            <li class="page-item">
                                <a class="page-link" href="/response/resumes/${resume.id}?page=0&size=${pageSize}" aria-label="First">
                                    <span aria-hidden="true">&laquo;&laquo;</span>
                                </a>
                            </li>
                            <li class="page-item">
                                <a class="page-link" href="/response/resumes/${resume.id}?page=${vacancies.number - 1}&size=${pageSize}" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                        </#if>

                        <#list 1..vacancies.totalPages as i>
                            <li class="page-item <#if i == vacancies.number + 1>active</#if>">
                                <a class="page-link" href="/response/resumes/${resume.id}?page=${i - 1}&size=${pageSize}">${i}</a>
                            </li>
                        </#list>

                        <#if vacancies.number lt vacancies.totalPages - 1>
                            <li class="page-item">
                                <a class="page-link" href="/response/resumes/${resume.id}?page=${vacancies.number + 1}&size=${pageSize}" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            </li>
                            <li class="page-item">
                                <a class="page-link" href="/response/resumes/${resume.id}?page=${vacancies.totalPages - 1}&size=${pageSize}" aria-label="Last">
                                    <span aria-hidden="true">&raquo;&raquo;</span>
                                </a>
                            </li>
                        </#if>
                    </ul>

                    <div class="d-flex justify-content-center align-items-center mt-3">
                        <span class="me-2"><@spring.message code="pagination"/></span>
                        <select class="form-select form-select-sm" style="width: auto;"
                                onchange="window.location.href='/response/resumes/${resume.id}?page=0&size='+this.value">
                            <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                            <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                            <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                            <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                        </select>
                    </div>
                </nav>
            </#if>
        <#else>
            <div class="card border-0 shadow-sm p-5 text-center">
                <div class="card-body">
                    <h3 class="mb-3 text-primary"><@spring.message code="response.resume.no.response"/></h3>
                    <a href="/vacancies" class="btn btn-primary mt-2">
                        <@spring.message code="vacancies.favorites.show"/>
                    </a>
                </div>
            </div>
        </#if>
    </div>
</@layout.page>
