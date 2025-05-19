<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/resumes/resumes.css" activeNav="resumes">
    <div class="container my-4">
        <div class="card resume-detail shadow-sm">
            <div class="card-body">
                <div class="d-flex flex-wrap justify-content-between align-items-center border-bottom pb-3 mb-3">
                    <div class="d-flex align-items-center">
                        <#if resume.applicantAvatar?? && resume.applicantAvatar?has_content>
                            <img src="${resume.applicantAvatar}" alt="Аватар" class="rounded-circle me-3" style="width: 60px; height: 60px;">
                        <#else>
                            <div class="rounded-circle bg-light d-flex justify-content-center align-items-center me-3" style="width: 60px; height: 60px;">
                            </div>
                        </#if>
                        <div>
                            <h1 class="h4 mb-1">${resume.name}</h1>
                            <div class="text-muted">
                                ${resume.applicantName}
                                <#if resume.applicantAge??>
                                    · ${resume.applicantAge} <@spring.message code="resumes.years"/>
                                </#if>
                            </div>
                        </div>
                    </div>
                    <div class="text-end bg-light p-3 rounded">
                        <div class="text-uppercase text-primary small fw-semibold">
                            <@spring.message code="resumes.salary"/>
                        </div>
                        <div class="h5 fw-bold">${resume.salary} с</div>
                    </div>
                </div>

                <div class="d-flex flex-wrap gap-3 mb-4 p-3 bg-light rounded">
                    <div class="d-flex align-items-center text-muted">
                        <i class="bi bi-clock me-2"></i>
                        <@spring.message code="resumes.date"/> ${resume.updateTime?string("dd.MM.yyyy")}
                    </div>
                    <div class="d-flex align-items-center text-muted">
                        <i class="bi bi-calendar me-2"></i>
                        <@spring.message code="resumes.createAt"/> ${resume.createDate?string("dd.MM.yyyy")}
                    </div>
                </div>

                <div class="mb-4">
                    <h2 class="h5 border-bottom pb-2 mb-3 d-flex align-items-center gap-2">
                        <i class="bi bi-briefcase"></i>
                        <@spring.message code="resumes.exp.title"/>
                    </h2>
                    <#if resume.workExperiences?? && resume.workExperiences?size gt 0>
                        <#list resume.workExperiences as exp>
                            <div class="mb-3 p-3 border rounded bg-white">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <div class="fw-semibold">${exp.companyName}</div>
                                        <div class="text-muted">${exp.position}</div>
                                    </div>
                                    <div class="text-muted small text-end">
                                        <#assign yearsText = ''>
                                        <#assign monthsText = ''>
                                        <#if exp.years gt 0>
                                            <#assign yearsText = exp.years + " " + springMacroRequestContext.getMessage("resumes.exp.year")>
                                        </#if>
                                        <#if exp.months gt 0>
                                            <#assign monthsText = exp.months + " " + springMacroRequestContext.getMessage("resumes.exp.month")>
                                        </#if>
                                        <@spring.message code="resumes.exp"/>:
                                        <#if yearsText?has_content && monthsText?has_content>
                                            ${yearsText} ${monthsText}
                                        <#elseif yearsText?has_content>
                                            ${yearsText}
                                        <#elseif monthsText?has_content>
                                            ${monthsText}
                                        <#else>
                                            <@spring.message code="resumes.exp.lessthanmonth"/>
                                        </#if>
                                    </div>
                                </div>
                                <#if exp.responsibilities?has_content>
                                    <div class="mt-2">${exp.responsibilities}</div>
                                </#if>
                            </div>
                        </#list>
                    <#else>
                        <div class="text-muted"><@spring.message code="resumes.exp.empty"/></div>
                    </#if>
                </div>

                <div class="mb-4">
                    <h2 class="h5 border-bottom pb-2 mb-3 d-flex align-items-center gap-2">
                        <i class="bi bi-mortarboard"></i>
                        <@spring.message code="resumes.edu.title"/>
                    </h2>
                    <#if resume.educationInfos?? && resume.educationInfos?size gt 0>
                        <#list resume.educationInfos as edu>
                            <div class="mb-3 p-3 border rounded bg-white">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <div class="fw-semibold">${edu.institution}</div>
                                        <div class="text-muted">${edu.degree}</div>
                                        <div class="text-muted">${edu.program}</div>
                                    </div>
                                    <div class="text-muted small">
                                        ${edu.startDate?string("yyyy")} - ${edu.endDate?string("yyyy")}
                                    </div>
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <div class="text-muted"><@spring.message code="resumes.edu.empty"/></div>
                    </#if>
                </div>

                <div class="mb-4">
                    <h2 class="h5 border-bottom pb-2 mb-3 d-flex align-items-center gap-2">
                        <i class="bi bi-telephone"></i>
                        <@spring.message code="resumes.contact.title"/>
                    </h2>
                    <#if resume.contactInfos?? && resume.contactInfos?size gt 0>
                        <div class="list-group">
                            <#list resume.contactInfos as contact>
                                <div class="list-group-item d-flex justify-content-between">
                                    <span>${contact.getTypeName()}</span>
                                    <span>${contact.value}</span>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <div class="text-muted"><@spring.message code="resumes.contact.desc"/></div>
                    </#if>
                </div>

                <div class="d-flex flex-wrap gap-2 mt-4">
                    <#if isApplicant>
                        <a href="/profile" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left"></i>
                            <@spring.message code="resumes.back.profile"/>
                        </a>
                        <a href="/resumes/${resume.id}/edit" class="btn btn-primary">
                            <i class="bi bi-pencil"></i>
                            <@spring.message code="card.btn.edit"/>
                        </a>
                        <form action="/resumes/${resume.id}/refresh" method="post" class="d-inline">
                            <#if _csrf??>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </#if>
                            <button type="submit" class="btn btn-outline-warning">
                                <@spring.message code="card.btn.refresh"/>
                            </button>
                        </form>
                        <form action="/resumes/${resume.id}/delete" method="post" class="d-inline">
                            <#if _csrf??>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </#if>
                            <button type="submit" class="btn btn-outline-danger">
                                <@spring.message code="card.btn.delete"/>
                            </button>
                        </form>
                    <#else>
                        <a href="/resumes/allResumes" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left"></i>
                            <@spring.message code="card.btn.back"/>
                        </a>
                    </#if>

                    <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('APPLICANT')>
                        <#if resume.applicantId??>
                            <a href="/profile/viewApplicant/${resume.applicantId}" class="btn btn-outline-info">
                                <@spring.message code="resumes.profile"/>
                            </a>
                        </#if>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</@layout.page>