<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/vacancies/vacancies.css" activeNav="vacancies">

    <main class="main">
        <div class="container">

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

            <div class="vacancy-detail">
                <div class="vacancy-header">
                    <h1 class="vacancy-title">${vacancy.name}</h1>
                    <div class="salary">${vacancy.salary} с</div>
                </div>

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
                    <@spring.message code="vacancies.card.author"/> ${vacancy.authorName}
                </span>
                    <span class="update-time">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                    </svg>
                    <@spring.message code="vacancies.card.date"/> ${vacancy.updateTime?string("dd.MM.yyyy")}
                </span>
                    <span class="create-time">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    <@spring.message code="vacancies.card.published"/> ${vacancy.createDate?string("dd.MM.yyyy")}
                </span>
                </div>

                <div class="section">
                    <h2 class="section-title"><@spring.message code="vacancies.card.title"/></h2>
                    <div class="section-content">${vacancy.description}</div>
                </div>

                <div class="section">
                    <h2 class="section-title"><@spring.message code="vacancies.card.requirements"/></h2>
                    <div class="section-content">
                        <p><@spring.message code="vacancies.card.exp.from"/>
                            ${vacancy.expFrom}
                            <@spring.message code="vacancies.card.exp.to"/>
                            ${vacancy.expTo}
                            <@spring.message code="vacancies.card.exp.years"/></p>
                    </div>
                </div>

                <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <#if vacancy.authorId??>
                        <a href="/profile/viewEmployer/${vacancy.authorId}" class="company-profile-btn">
                            <@spring.message code="vacancies.company.profile"/>
                        </a>
                        <a href="#modal-${vacancy.id}" class="respond-btn"><@spring.message code="vacancies.respond.button"/></a>
                    </#if>
                </#if>

                <div id="modal-${vacancy.id}" class="modal">
                    <div class="modal-content">
                        <a href="#" class="close-modal">&times;</a>
                        <h3 class="modal-title"><@spring.message code="vacancies.response.modal.title"/> "${vacancy.name}"</h3>

                        <form action="/response/respond" method="post">
                            <input type="hidden" name="vacancyId" value="${vacancy.id}">
                            <#if _csrf??>
                                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                            </#if>

                            <label for="resumeId-${vacancy.id}"><@spring.message code="vacancies.response.select.resume"/></label>
                            <select id="resumeId-${vacancy.id}" name="resumeId" class="resume-select">
                                <#if resumesByUser?? && resumesByUser?size gt 0>
                                    <#list resumesByUser as resume>
                                        <option value="${resume.id}">${resume.name} (${resume.updateTime?string("dd.MM.yyyy")})</option>
                                    </#list>
                                <#else>
                                    <option value="" disabled><@spring.message code="vacancies.response.no.resumes"/></option>
                                </#if>
                            </select>

                            <button type="submit" class="submit-resume-btn">
                                <@spring.message code="vacancies.response.submit.button"/>
                            </button>
                        </form>
                    </div>
                </div>

                <div class="actions">
                    <a href="/vacancies" class="btn btn-back"><@spring.message code="card.btn.back"/></a>
                    <#if isAuthor>
                        <a href="/vacancies/${vacancy.id}/edit" class="btn btn-edit"><@spring.message code="card.btn.edit"/></a>
                        <form action="/vacancies/${vacancy.id}/refresh" method="post">
                            <#if _csrf??>
                                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                            </#if>
                            <button type="submit" class="btn btn-refresh"><@spring.message code="card.btn.refresh"/></button>
                        </form>
                        <form action="/vacancies/${vacancy.id}/delete" method="post">
                            <#if _csrf??>
                                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                            </#if>
                            <button type="submit" class="btn btn-delete"><@spring.message code="card.btn.delete"/></button>
                        </form>
                    </#if>
                </div>
            </div>
        </div>
    </main>

</@layout.page>
