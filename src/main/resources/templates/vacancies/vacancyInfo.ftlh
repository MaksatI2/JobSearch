<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/vacancies/vacancies.css" activeNav="vacancies">
    <script src="/static/Js/notifications.js"></script>
    <div class="container py-4">
        <#if successMessage??>
            <div class="alert alert-success mb-4">
                ${successMessage}
            </div>
        </#if>

        <#if errorMessage??>
            <div class="alert alert-danger mb-4">
                ${errorMessage}
            </div>
        </#if>

        <div class="card vacancy-card border-0 shadow-sm p-4 mb-4">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start mb-3">
                    <h1 class="card-title profile-title">${vacancy.name}</h1>
                    <span class="badge bg-primary rounded-pill fs-3">${vacancy.salary} с</span>
                </div>

                <div class="d-flex flex-wrap gap-3 text-muted mb-4">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-clock-history me-1"></i>
                        <@spring.message code="vacancies.card.exp"/> ${vacancy.expFrom}-${vacancy.expTo}
                        <@spring.message code="vacancies.card.exp.years"/>
                    </div>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-person me-1"></i>
                        <@spring.message code="vacancies.card.author"/> ${vacancy.authorName}
                    </div>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-calendar me-1"></i>
                        <@spring.message code="vacancies.card.date"/> ${vacancy.updateTime?string("dd.MM.yyyy")}
                    </div>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-calendar-check me-1"></i>
                        <@spring.message code="vacancies.card.published"/> ${vacancy.createDate?string("dd.MM.yyyy")}
                    </div>
                </div>

                <div class="mb-4">
                    <h2 class="section-title"><@spring.message code="vacancies.card.title"/></h2>
                    <p class="text-dark">${vacancy.description}</p>
                </div>

                <div class="mb-4">
                    <h2 class="section-title"><@spring.message code="vacancies.card.requirements"/></h2>
                    <p class="text-dark">
                        <@spring.message code="vacancies.card.exp.from"/>
                        ${vacancy.expFrom}
                        <@spring.message code="vacancies.card.exp.to"/>
                        ${vacancy.expTo}
                        <@spring.message code="vacancies.card.exp.years"/>
                    </p>
                </div>

                <div class="d-flex flex-wrap gap-2">
                    <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                        <#if vacancy.authorId??>
                            <a href="/profile/viewEmployer/${vacancy.authorId}" class="btn btn-primary">
                                <@spring.message code="vacancies.company.profile"/>
                            </a>
                            <a href="#modal-${vacancy.id}" class="btn btn-primary">
                                <@spring.message code="vacancies.respond.button"/>
                            </a>
                        </#if>
                    </#if>
                </div>
            </div>
        </div>

        <div id="modal-${vacancy.id}" class="modal-vacancy">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"><@spring.message code="vacancies.response.modal.title"/> "${vacancy.name}"</h5>
                        <a href="#" class="btn-close" aria-label="Close"></a>
                    </div>
                    <div class="modal-body">
                        <form action="/response/respond" method="post">
                            <input type="hidden" name="vacancyId" value="${vacancy.id}">
                            <#if _csrf??>
                                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                            </#if>

                            <div class="mb-3">
                                <label for="resumeId-${vacancy.id}" class="form-label"><@spring.message code="vacancies.response.select.resume"/></label>
                                <select id="resumeId-${vacancy.id}" name="resumeId" class="form-select">
                                    <#if resumesByUser?? && resumesByUser?size gt 0>
                                        <#list resumesByUser as resume>
                                            <option value="${resume.id}">${resume.name} (${resume.updateTime?string("dd.MM.yyyy")})</option>
                                        </#list>
                                    <#else>
                                        <option value="" disabled><@spring.message code="vacancies.response.no.resumes"/></option>
                                    </#if>
                                </select>
                            </div>

                            <button type="submit" class="btn btn-primary w-100">
                                <@spring.message code="vacancies.response.submit.button"/>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="d-flex flex-wrap gap-2">
            <a href="/vacancies" class="btn btn-secondary">
                <i class="bi bi-arrow-left me-1"></i>
                <@spring.message code="card.btn.back"/>
            </a>
            <#if isAuthor>
                <a href="/vacancies/${vacancy.id}/edit" class="btn btn-warning">
                    <i class="bi bi-pencil me-1"></i>
                    <@spring.message code="card.btn.edit"/>
                </a>
                <form action="/vacancies/${vacancy.id}/refresh" method="post" class="d-inline">
                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                    </#if>
                    <button type="submit" class="btn btn-success">
                        <i class="bi bi-arrow-clockwise me-1"></i>
                        <@spring.message code="card.btn.refresh"/>
                    </button>
                </form>
                <form action="/vacancies/${vacancy.id}/delete" method="post" class="d-inline">
                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                    </#if>
                    <button type="submit" class="btn btn-danger">
                        <i class="bi bi-trash me-1"></i>
                        <@spring.message code="card.btn.delete"/>
                    </button>
                </form>
            </#if>
        </div>
    </div>
</@layout.page>
