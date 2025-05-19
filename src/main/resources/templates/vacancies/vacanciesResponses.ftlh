<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/vacancies/vacancies.css" activeNav="profile">
    <div class="container py-4">
        <h1 class="profile-title mb-4"><@spring.message code="vacancies.responded.title"/></h1>

        <div class="mb-4">
            <a href="/profile" class="btn btn-outline-secondary">
                <i class="bi bi-arrow-left me-1"></i>
                <@spring.message code="vacancies.responded.back.profile"/>
            </a>
        </div>

        <#if vacancies?size == 0>
            <div class="card border-0 shadow-sm p-5 text-center">
                <div class="card-body">
                    <h3 class="mb-3 text-primary"><@spring.message code="vacancies.responded.no.vacancies"/></h3>
                    <p class="text-muted"><@spring.message code="vacancies.responded.empty.vacancies"/></p>
                </div>
            </div>
        </#if>

        <div class="row g-4">
            <#list vacancies as vacancy>
                <div class="col-lg-6">
                    <div class="card vacancy-card h-100 border-0 shadow-sm">
                        <div class="card-body p-4">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <a href="/vacancies/${vacancy.id}/info" class="text-decoration-none">
                                    <h5 class="card-title text-primary">${vacancy.name}</h5>
                                </a>
                                <span class="badge bg-primary rounded-pill fs-5">${vacancy.salary?string} c</span>
                            </div>

                            <p class="text-muted mb-3">${vacancy.description}</p>

                            <div class="d-flex flex-wrap gap-3 text-muted mb-3">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-clock-history me-1"></i>
                                    <small><@spring.message code="vacancies.card.exp"/> ${vacancy.expFrom}-${vacancy.expTo}
                                        <@spring.message code="vacancies.card.exp.years"/></small>
                                </div>
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-chat-text me-1"></i>
                                    <small><@spring.message code="vacancies.responded.count"/> ${vacancy.responsesCount!0}</small>
                                </div>
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-calendar me-1"></i>
                                    <small><@spring.message code="vacancies.card.date"/> ${vacancy.updateTime?string("dd.MM.yyyy")}</small>
                                </div>
                            </div>

                            <div class="d-grid">
                                <a href="/response/vacancies/${vacancy.id}" class="btn btn-primary">
                                    <@spring.message code="vacancies.responded.count.applicant"/> (${vacancy.responsesCount})
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </#list>
        </div>

        <#if totalPages gt 1>
            <nav aria-label="Page navigation" class="mt-4">
                <ul class="pagination justify-content-center">
                    <#if currentPage gt 0>
                        <li class="page-item">
                            <a class="page-link" href="/vacancies/${userId}/response?page=0&size=${pageSize}" aria-label="First">
                                <span aria-hidden="true">&laquo;&laquo;</span>
                            </a>
                        </li>
                        <li class="page-item">
                            <a class="page-link" href="/vacancies/${userId}/response?page=${currentPage - 1}&size=${pageSize}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </#if>

                    <#list 0..totalPages-1 as i>
                        <li class="page-item <#if i == currentPage>active</#if>">
                            <a class="page-link" href="/vacancies/${userId}/response?page=${i}&size=${pageSize}">${i + 1}</a>
                        </li>
                    </#list>

                    <#if currentPage lt totalPages - 1>
                        <li class="page-item">
                            <a class="page-link" href="/vacancies/${userId}/response?page=${currentPage + 1}&size=${pageSize}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                        <li class="page-item">
                            <a class="page-link" href="/vacancies/${userId}/response?page=${totalPages - 1}&size=${pageSize}" aria-label="Last">
                                <span aria-hidden="true">&raquo;&raquo;</span>
                            </a>
                        </li>
                    </#if>
                </ul>

                <div class="d-flex justify-content-center align-items-center mt-3">
                    <span class="me-2"><@spring.message code="pagination"/></span>
                    <select class="form-select form-select-sm" style="width: auto;"
                            onchange="window.location.href='/vacancies/${userId}/response?page=0&size='+this.value">
                        <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                        <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                        <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                        <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                    </select>
                </div>
            </nav>
        </#if>
    </div>
</@layout.page>
