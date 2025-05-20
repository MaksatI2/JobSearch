<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/resumes/resumes.css" activeNav="profile">
    <div class="container py-4">
        <div class="mb-4">
            <a href="/profile" class="btn btn-outline-secondary">
                <i class="bi bi-arrow-left me-1"></i>
                <@spring.message code="response.back"/>
            </a>
        </div>

        <#if resumes.content?? && resumes.content?has_content>
            <div class="row g-4">
                <#list resumes.content as resume>
                    <div class="col-lg-6">
                        <a href="/resumes/${resume.id}/info" class="text-decoration-none">
                        <div class="card resume-card h-100 border-0 shadow-sm">
                            <div class="card-body p-4">
                                <div class="d-flex justify-content-between align-items-start mb-3">
                                        <h5 class="card-title text-primary">${resume.name}</h5>
                                    <span class="badge bg-primary rounded-pill fs-5">${resume.salary} с</span>
                                </div>

                                <div class="d-flex align-items-center mb-3">
                                    <#if resume.applicantAvatar?? && resume.applicantAvatar?has_content>
                                        <img src="${resume.applicantAvatar}" alt="Аватар" class="rounded-circle me-2" style="width: 40px; height: 40px; object-fit: cover;">
                                    <#else>
                                        <div class="d-flex align-items-center justify-content-center bg-light rounded-circle me-2" style="width: 40px; height: 40px;">
                                            <i class="bi bi-person"></i>
                                        </div>
                                    </#if>
                                    <div>
                                        <span class="fw-medium">${resume.applicantName}</span>
                                        <#if resume.applicantAge??>
                                            <span class="text-muted ms-2">${resume.applicantAge} <@spring.message code="resumes.years"/></span>
                                        </#if>
                                    </div>
                                </div>

                                <div class="d-flex justify-content-end align-items-center text-muted mb-3">
                                    <i class="bi bi-calendar me-1"></i>
                                    <small>${resume.updateTime?string("dd.MM.yyyy")}</small>
                                </div>

                                <div class="d-grid">
                                    <a href="/profile/viewApplicant/${resume.applicantId}" class="btn btn-primary">
                                        <@spring.message code="resumes.profile"/>
                                    </a>
                                </div>
                            </div>
                        </div>
                        </a>
                    </div>
                </#list>
            </div>

            <#if resumes.totalPages gt 1>
                <nav aria-label="Page navigation" class="mt-4">
                    <ul class="pagination justify-content-center">
                        <#if resumes.number gt 0>
                            <li class="page-item">
                                <a class="page-link" href="/response/vacancies/${vacancy.id}?page=0&size=${pageSize}" aria-label="First">
                                    <span aria-hidden="true">&laquo;&laquo;</span>
                                </a>
                            </li>
                            <li class="page-item">
                                <a class="page-link" href="/response/vacancies/${vacancy.id}?page=${resumes.number - 1}&size=${pageSize}" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                        </#if>

                        <#list 1..resumes.totalPages as i>
                            <li class="page-item <#if i == resumes.number + 1>active</#if>">
                                <a class="page-link" href="/response/vacancies/${vacancy.id}?page=${i - 1}&size=${pageSize}">${i}</a>
                            </li>
                        </#list>

                        <#if resumes.number lt resumes.totalPages - 1>
                            <li class="page-item">
                                <a class="page-link" href="/response/vacancies/${vacancy.id}?page=${resumes.number + 1}&size=${pageSize}" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            </li>
                            <li class="page-item">
                                <a class="page-link" href="/response/vacancies/${vacancy.id}?page=${resumes.totalPages - 1}&size=${pageSize}" aria-label="Last">
                                    <span aria-hidden="true">&raquo;&raquo;</span>
                                </a>
                            </li>
                        </#if>
                    </ul>

                    <div class="d-flex justify-content-center align-items-center mt-3">
                        <span class="me-2"><@spring.message code="pagination"/></span>
                        <select class="form-select form-select-sm" style="width: auto;"
                                onchange="window.location.href='/response/vacancies/${vacancy.id}?page=0&size='+this.value">
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
                    <h3 class="mb-3 text-primary">На эту вакансию пока нет откликов</h3>
                    <p class="text-muted">Попробуйте зайти позже</p>
                </div>
            </div>
        </#if>
    </div>
</@layout.page>