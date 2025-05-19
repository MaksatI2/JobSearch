<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/user/profile.css" activeNav="profile">
    <div class="container py-4">
        <h2 class="profile-title mb-4"><@spring.message code="profile.title"/>
            <@spring.message code="profile.employer.title"/></h2>

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
                            <h5 class="mb-3 fw-bold">${user.name}</h5>
                            <p class="mb-2 text-muted">Email: ${user.email}</p>
                            <p class="mb-0 text-muted"><@spring.message code="profile.phone"/> ${user.phoneNumber}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-8">
                <div class="card shadow-sm border-0 rounded-3">
                    <div class="card-body p-4">
                        <h3 class="profile-subtitle mb-4"><@spring.message code="employer.vacancies.title"/></h3>

                        <div class="vacancy-list">
                            <#list vacancies![] as vacancy>
                                <#if vacancy.id??>
                                    <div class="card vacancy-card mb-3 shadow border-0 rounded-3">
                                        <div class="card-body p-4">
                                            <a href="/vacancies/${vacancy.id}/info" class="text-decoration-none">
                                                <h5 class="card-title fw-bold mb-2">${vacancy.name}</h5>
                                                <p class="text-muted mb-3"><@spring.message code="vacancies.card.date"/> ${vacancy.updateTime}</p>
                                            </a>
                                        </div>
                                    </div>
                                <#else>
                                    <div class="alert alert-info">
                                        <@spring.message code="vacancies.empty.title"/>
                                    </div>
                                </#if>
                            </#list>

                            <#if totalVacancyPages?? && (totalVacancyPages > 1)>
                                <nav class="mt-4">
                                    <ul class="pagination justify-content-center">
                                        <#if (currentVacancyPage > 1)>
                                            <li class="page-item">
                                                <a class="page-link" href="/profile/viewEmployer/${userId}?page=${currentVacancyPage - 1}" aria-label="Previous">
                                                    <span aria-hidden="true">&laquo;</span>
                                                </a>
                                            </li>
                                        </#if>

                                        <#list 1..totalVacancyPages as page>
                                            <li class="page-item <#if page == currentVacancyPage>active</#if>">
                                                <a class="page-link" href="/profile/viewEmployer/${userId}?page=${page}">${page}</a>
                                            </li>
                                        </#list>

                                        <#if (currentVacancyPage < totalVacancyPages)>
                                            <li class="page-item">
                                                <a class="page-link" href="/profile/viewEmployer/${userId}?page=${currentVacancyPage + 1}" aria-label="Next">
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