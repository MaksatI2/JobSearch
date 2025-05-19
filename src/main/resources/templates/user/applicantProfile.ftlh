<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/user/profile.css" activeNav="profile">
    <script src="/static/Js/notifications.js"></script>
    <div class="container py-4">
        <h2 class="profile-title mb-4"><@spring.message code="profile.title"/>
            <@spring.message code="profile.applicant.title"/></h2>

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
                        </div>

                        <div class="user-info mb-4">
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
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-8">
                <div class="card shadow-sm border-0 rounded-3">
                    <div class="card-body p-4">
                        <h3 class="profile-subtitle mb-4"><@spring.message code="applicant.resumes.title"/></h3>

                        <div class="resume-list">
                            <#list resumes![] as resume>
                                <#if resume.id??>
                                    <div class="card resume-card mb-3 shadow border-0 rounded-3">
                                        <div class="card-body p-4">
                                            <a href="/resumes/${resume.id}/info" class="text-decoration-none">
                                                <h5 class="card-title fw-bold mb-2">${resume.name!""}</h5>
                                                <p class="text-muted mb-3"><@spring.message code="resumes.date"/> ${resume.updateTime!""}</p>
                                            </a>
                                        </div>
                                    </div>
                                <#else>
                                    <div class="alert alert-info">
                                        <@spring.message code="profile.applicant.resumes.empty"/>
                                    </div>
                                </#if>
                            </#list>

                            <#if totalResumePages?? && (totalResumePages > 1)>
                                <nav class="mt-4">
                                    <ul class="pagination justify-content-center">
                                        <#if (currentResumePage > 1)>
                                            <li class="page-item">
                                                <a class="page-link" href="/profile/viewApplicant/${userId}?page=${currentResumePage - 1}" aria-label="Previous">
                                                    <span aria-hidden="true">&laquo;</span>
                                                </a>
                                            </li>
                                        </#if>

                                        <#list 1..totalResumePages as page>
                                            <li class="page-item <#if page == currentResumePage>active</#if>">
                                                <a class="page-link" href="/profile/viewApplicant/${userId}?page=${page}">${page}</a>
                                            </li>
                                        </#list>

                                        <#if (currentResumePage < totalResumePages)>
                                            <li class="page-item">
                                                <a class="page-link" href="/profile/viewApplicant/${userId}?page=${currentResumePage + 1}" aria-label="Next">
                                                    <span aria-hidden="true">&raquo;</span>
                                                </a>
                                            </li>
                                        </#if>
                                    </ul>
                                </nav>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.page>