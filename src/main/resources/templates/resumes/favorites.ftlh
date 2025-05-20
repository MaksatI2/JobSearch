<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/resumes/resumes.css" activeNav="favorite">
    <div class="container py-4">
        <h1 class="mb-4 profile-title"><@spring.message code="resumes.favorites.title"/></h1>
        <script src="/static/Js/notifications.js"></script>

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

        <div class="mb-4">
            <a href="/resumes/allResumes" class="btn btn-primary">
                <i class="bi bi-arrow-left me-1"></i>
                <@spring.message code="resumes.favorites.back.resumes"/>
            </a>
        </div>

        <#if resumes?size == 0>
            <div class="card border-0 shadow-sm p-5 text-center">
                <div class="card-body">
                    <h3 class="mb-3 text-primary"><@spring.message code="resumes.favorites.no.vacancies"/></h3>
                    <p class="text-muted mb-4"><@spring.message code="resumes.favorites.empty.resumes"/></p>
                    <a href="/resumes/allResumes" class="btn btn-primary">
                        <@spring.message code="resumes.favorites.show"/>
                    </a>
                </div>
            </div>
        <#else>
            <div class="row g-4">
                <#list resumes as resume>
                    <div class="col-lg-6">
                        <a href="/resumes/${resume.id}/info" class="text-decoration-none">
                        <div class="card resume-card h-100 border-0 shadow-sm">
                            <div class="card-body p-4 position-relative">
                                <div class="d-flex justify-content-between align-items-start mb-3">
                                    <h5 class="card-title text-primary">${resume.name}</h5>
                                    <span class="badge bg-primary rounded-pill fs-5">${resume.salary} с</span>
                                </div>

                                <div class="d-flex align-items-center mb-3">
                                    <#if resume.applicantAvatar?? && resume.applicantAvatar?has_content>
                                        <img src="/api/users/${resume.applicantId}/avatar" alt="Аватар"  onerror="this.src='/static/default-avatar/default_avatar.jpg'" class="rounded-circle me-2" style="width: 40px; height: 40px; object-fit: cover;">
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

                                <div class="d-flex justify-content-end align-items-center text-muted">
                                    <i class="bi bi-calendar me-1"></i>
                                    <small>${resume.updateTime?string("dd.MM.yyyy")}</small>
                                </div>

                                <div class="position-absolute bottom-0 end-0 mb-3 me-3">
                                    <form action="/favorites/resumes/${resume.id}/remove" method="post">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <button type="submit" class="btn btn-sm btn-danger">
                                            <i class="bi bi-trash me-1"></i>
                                            <@spring.message code="favorites.delete"/>
                                        </button>
                                    </form>
                                </div>

                                <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('APPLICANT')>
                                    <#if resume.applicantId??>
                                        <a href="/profile/viewApplicant/${resume.applicantId}" class="btn btn-sm btn-primary mt-3">
                                            <@spring.message code="resumes.profile"/>
                                        </a>
                                    </#if>
                                </#if>
                            </div>
                        </div>
                        </a>
                    </div>
                </#list>
            </div>
        </#if>
    </div>
</@layout.page>
