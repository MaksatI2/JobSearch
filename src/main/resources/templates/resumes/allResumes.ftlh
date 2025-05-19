<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/resumes/resumes.css" activeNav="resumes">
    <script src="/static/Js/notifications.js"></script>

    <div class="container py-4">
        <h1 class="mb-4 profile-title"><@spring.message code="resumes.title"/></h1>

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

        <div class="card shadow-sm mb-4 border-0 rounded-3">
            <div class="card-body p-3">
                <form method="get" action="/resumes/allResumes" class="row g-2 align-items-end">
                    <input type="hidden" name="page" value="0">
                    <input type="hidden" name="size" value="${pageSize}">

                    <div class="col-md-4">
                        <label for="sort" class="form-label"><@spring.message code="sort.label"/></label>
                        <select id="sort" name="sort" class="form-select">
                            <option value=""><@spring.message code="sort.date"/></option>
                            <option value="salaryAsc" <#if selectedSort?? && selectedSort == "salaryAsc">selected</#if>>
                                <@spring.message code="sort.salary.asc"/>
                            </option>
                            <option value="salaryDesc" <#if selectedSort?? && selectedSort == "salaryDesc">selected</#if>>
                                <@spring.message code="sort.salary.desc"/>
                            </option>
                        </select>
                    </div>

                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">
                            <@spring.message code="filter.apply"/>
                        </button>
                    </div>

                    <#if selectedSort??>
                        <div class="col-auto">
                            <a href="/resumes/allResumes" class="btn btn-outline-secondary">
                                <@spring.message code="filter.reset"/>
                            </a>
                        </div>
                    </#if>
                </form>
            </div>
        </div>

        <#if resumes?? && resumes?has_content>
            <div class="row g-4">
                <#list resumes as resume>
                    <div class="col-lg-12">
                        <a href="/resumes/${resume.id}/info" class="text-decoration-none">
                        <div class="card resume-card h-100 border-0 shadow-sm">
                            <div class="card-body p-4 position-relative">
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
                                        <span class="fw-medium">${resume.applicantName!'Неизвестно'}</span>
                                        <#if resume.applicantAge??>
                                            <span class="text-muted ms-2">${resume.applicantAge} <@spring.message code="resumes.years"/></span>
                                        </#if>
                                    </div>
                                </div>

                                <div class="d-flex justify-content-end align-items-center text-muted">
                                    <i class="bi bi-calendar me-1"></i>
                                    <small>${resume.updateTime?string("dd.MM.yyyy")}</small>
                                </div>

                                <form action="/favorites/resumes/${resume.id}/add" method="post" class="position-absolute top-0 end-0 mt-2 me-2">
                                    <input type="hidden" name="resumeId" value="${resume.id}">
                                    <#if _csrf??>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    </#if>
                                    <button type="submit" class="btn btn-link text-warning p-0 border-0" title="Добавить в избранное">
                                        <i class="bi bi-bookmark-star<#if favoriteResumeIds?? && favoriteResumeIds?seq_contains(resume.id)>-fill</#if> fs-4"></i>
                                    </button>
                                </form>

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

            <#if totalPages gt 1>
                <nav aria-label="Page navigation" class="mt-4">
                    <ul class="pagination justify-content-center">
                        <#if currentPage gt 0>
                            <li class="page-item">
                                <a class="page-link" href="/resumes/allResumes?page=0&size=${pageSize}" aria-label="First">
                                    <span aria-hidden="true">&laquo;&laquo;</span>
                                </a>
                            </li>
                            <li class="page-item">
                                <a class="page-link" href="/resumes/allResumes?page=${currentPage - 1}&size=${pageSize}" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                        </#if>

                        <#list 0..totalPages-1 as i>
                            <li class="page-item <#if i == currentPage>active</#if>">
                                <a class="page-link" href="/resumes/allResumes?page=${i}&size=${pageSize}">${i + 1}</a>
                            </li>
                        </#list>

                        <#if currentPage lt totalPages - 1>
                            <li class="page-item">
                                <a class="page-link" href="/resumes/allResumes?page=${currentPage + 1}&size=${pageSize}" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            </li>
                            <li class="page-item">
                                <a class="page-link" href="/resumes/allResumes?page=${totalPages - 1}&size=${pageSize}" aria-label="Last">
                                    <span aria-hidden="true">&raquo;&raquo;</span>
                                </a>
                            </li>
                        </#if>
                    </ul>

                    <div class="d-flex justify-content-center align-items-center mt-3">
                        <span class="me-2"><@spring.message code="pagination"/></span>
                        <select class="form-select form-select-sm" style="width: auto;"
                                onchange="window.location.href='/resumes/allResumes?page=0&size='+this.value">
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
                    <h3 class="mb-3 text-primary"><@spring.message code="resumes.empty"/></h3>
                    <p class="text-muted mb-0"><@spring.message code="resumes.message"/></p>
                </div>
            </div>
        </#if>
    </div>

</@layout.page>
