<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/user/profile.css" activeNav="profile">
    <script src="/static/Js/notifications.js"></script>
    <div class="container py-4">
        <h2 class="profile-title mb-4"><@spring.message code="profile.title"/>
            <#if user.accountType == "APPLICANT">
                <@spring.message code="profile.applicant.title"/>
            <#else>
                <@spring.message code="profile.employer.title"/>
            </#if>
        </h2>

        <#if successMessage??>
            <div class="alert alert-success mb-4 shadow-sm">
                ${successMessage}
            </div>
        </#if>

        <#if errorMessage??>
            <div class="alert alert-danger mb-4 shadow-sm">
                ${errorMessage}
            </div>
        </#if>

        <div class="row g-4">
            <div class="col-md-4">
                <div class="card shadow-sm h-100 border-0 rounded-3">
                    <div class="card-body text-center p-4">
                        <div class="avatar-container mb-3">
                            <img src="/api/users/${user.id}/avatar" alt="Аватар" class="avatar-image mx-auto d-block mb-2"
                                 onerror="this.src='/static/default-avatar/default_avatar.jpg'">
                            <a href="/profile/avatar/edit" class="btn btn-primary btn-sm edit-button mt-2">
                                <@spring.message code="profile.edit.avatar"/>
                            </a>
                        </div>

                        <div class="user-info mb-4">
                            <#if user.accountType == "APPLICANT">
                                <h5 class="mb-3 fw-bold">${user.name} ${user.surname}</h5>
                                <p class="mb-2 text-muted">Email: ${user.email}</p>
                                <p class="mb-2 text-muted">
                                    <@spring.message code="profile.applicant.age"/>
                                    <#if user.age??>${user.age}
                                    <#else>
                                        <span class="text-danger"><@spring.message code="profile.empty.user.info"/></span>
                                    </#if>
                                </p>
                                <p class="mb-0 text-muted"><@spring.message code="profile.phone"/> ${user.phoneNumber}</p>
                            <#else>
                                <h5 class="mb-3 fw-bold">${user.name}</h5>
                                <p class="mb-2 text-muted">Email: ${user.email}</p>
                                <p class="mb-0 text-muted"><@spring.message code="profile.phone"/> ${user.phoneNumber}</p>
                            </#if>
                        </div>

                        <a href="/profile/edit" class="btn btn-primary w-100 edit-button">
                            <@spring.message code="profile.edit"/>
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-8">
                <div class="card shadow-sm border-0 rounded-3">
                    <div class="card-body p-4">
                        <#if user.accountType == "APPLICANT">
                            <div class="d-flex flex-wrap justify-content-between align-items-center mb-4">
                                <a href="/resumes/create" class="btn btn-success action-button mb-2 mb-md-0">
                                    <@spring.message code="profile.applicant.create.resume"/>
                                </a>

                                <a href="/resumes/${user.id}/response" class="btn btn-primary responses-button position-relative">
                                    <@spring.message code="profile.responses"/>
                                    <span class="responses-badge">${responsesCount}</span>
                                </a>
                            </div>

                            <h3 class="profile-subtitle mb-4 mt-4"><@spring.message code="profile.applicant.resumes.title"/></h3>

                            <div class="resume-list">
                                <#list resumes![] as resume>
                                    <#if resume.id??>
                                        <div class="card resume-card mb-3 shadow border-0 rounded-3">
                                            <div class="card-body p-4">
                                                <a href="/resumes/${resume.id}/info" class="text-decoration-none">
                                                    <h5 class="card-title fw-bold mb-2">${resume.name!""}</h5>
                                                    <p class="text-muted mb-3"><@spring.message code="resumes.date"/> ${resume.updateTime!""}</p>
                                                </a>
                                                <div class="d-flex">
                                                    <form action="/resumes/${resume.id}/refresh" method="post" class="me-2">
                                                        <#if _csrf??>
                                                            <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                                                                   value="${(_csrf.token)!'csrf-token'}"/>
                                                        </#if>
                                                        <button type="submit" class="btn btn-success btn-sm refresh-button">
                                                            <@spring.message code="card.btn.refresh"/>
                                                        </button>
                                                    </form>
                                                    <a href="/resumes/${resume.id}/edit" class="btn btn-primary btn-sm edit-button">
                                                        <@spring.message code="card.btn.edit"/>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    <#else>
                                        <div class="alert alert-info">
                                            <@spring.message code="profile.applicant.resumes.empty"/>
                                        </div>
                                    </#if>
                                <#else>
                                    <div class="alert alert-info">
                                        <@spring.message code="vacancies.response.no.resumes"/>
                                    </div>
                                </#list>
                            </div>

                            <#if totalResumePages?? && (totalResumePages > 1)>
                                <nav class="mt-4">
                                    <ul class="pagination justify-content-center">
                                        <#if (currentResumePage > 1)>
                                            <li class="page-item">
                                                <a class="page-link" href="/profile?resumePage=${currentResumePage - 1}" aria-label="Previous">
                                                    <span aria-hidden="true">&laquo;</span>
                                                </a>
                                            </li>
                                        </#if>

                                        <#list 1..totalResumePages as page>
                                            <li class="page-item <#if page == currentResumePage>active</#if>">
                                                <a class="page-link" href="/profile?resumePage=${page}">${page}</a>
                                            </li>
                                        </#list>

                                        <#if (currentResumePage < totalResumePages)>
                                            <li class="page-item">
                                                <a class="page-link" href="/profile?resumePage=${currentResumePage + 1}" aria-label="Next">
                                                    <span aria-hidden="true">&raquo;</span>
                                                </a>
                                            </li>
                                        </#if>
                                    </ul>
                                </nav>
                            </#if>

                        <#elseif user.accountType == "EMPLOYER">
                            <div class="d-flex flex-wrap justify-content-between align-items-center mb-4">
                                <a href="/vacancies/create" class="btn btn-success action-button mb-2 mb-md-0">
                                    <@spring.message code="profile.employer.create.vacancies"/>
                                </a>

                                <a href="/vacancies/${user.id}/response" class="btn btn-primary responses-button position-relative">
                                    <@spring.message code="profile.responses"/>
                                    <span class="responses-badge">${responsesCount}</span>
                                </a>
                            </div>

                            <h3 class="profile-subtitle mb-4 mt-4"><@spring.message code="profile.employer.vacancies.title"/></h3>

                            <div class="vacancy-list">
                                <#list vacancies![] as vacancy>
                                    <#if vacancy.id??>
                                        <div class="card vacancy-card mb-3 shadow border-0 rounded-3">
                                            <div class="card-body p-4">
                                                <a href="/vacancies/${vacancy.id}/info" class="text-decoration-none">
                                                    <h5 class="card-title fw-bold mb-2">${vacancy.name}</h5>
                                                    <p class="text-muted mb-3"><@spring.message code="vacancies.card.date"/> ${vacancy.updateTime}</p>
                                                </a>
                                                <div class="d-flex">
                                                    <form action="/vacancies/${vacancy.id}/refresh" method="post" class="me-2">
                                                        <#if _csrf??>
                                                            <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                                                                   value="${(_csrf.token)!'csrf-token'}"/>
                                                        </#if>
                                                        <button type="submit" class="btn btn-success btn-sm refresh-button">
                                                            <@spring.message code="card.btn.refresh"/>
                                                        </button>
                                                    </form>
                                                    <a href="/vacancies/${vacancy.id}/edit" class="btn btn-primary btn-sm edit-button">
                                                        <@spring.message code="card.btn.edit"/>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    <#else>
                                        <div class="alert alert-info">
                                            <@spring.message code="profile.employer.vacancies.empty"/>
                                        </div>
                                    </#if>
                                </#list>
                            </div>

                            <#if totalVacancyPages?? && (totalVacancyPages > 1)>
                                <nav class="mt-4">
                                    <ul class="pagination justify-content-center">
                                        <#if (currentVacancyPage > 1)>
                                            <li class="page-item">
                                                <a class="page-link" href="/profile?vacancyPage=${currentVacancyPage - 1}" aria-label="Previous">
                                                    <span aria-hidden="true">&laquo;</span>
                                                </a>
                                            </li>
                                        </#if>

                                        <#list 1..totalVacancyPages as page>
                                            <li class="page-item <#if page == currentVacancyPage>active</#if>">
                                                <a class="page-link" href="/profile?vacancyPage=${page}">${page}</a>
                                            </li>
                                        </#list>

                                        <#if (currentVacancyPage < totalVacancyPages)>
                                            <li class="page-item">
                                                <a class="page-link" href="/profile?vacancyPage=${currentVacancyPage + 1}" aria-label="Next">
                                                    <span aria-hidden="true">&raquo;</span>
                                                </a>
                                            </li>
                                        </#if>
                                    </ul>
                                </nav>
                            </#if>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.page>