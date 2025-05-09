<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/user/profile.css" activeNav="profile">
    <h2><@spring.message code="profile.title"/>
        <@spring.message code="profile.employer.title"/></h2>

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

    <div class="profile">
        <div class="left-column">
            <div class="avatar-container">
                <img src="/api/users/${user.id}/avatar"
                     alt="Аватар"
                     onerror="this.src='/static/default-avatar/default_avatar.jpg'">
            </div>

            <p>${user.name}</p>
            <p>Email: ${user.email}</p>
            <p><@spring.message code="profile.phone"/> ${user.phoneNumber}</p>
        </div>

        <div class="right-column">
            <h3><@spring.message code="employer.vacancies.title"/></h3>
            <#list vacancies![] as vacancy>
                <#if vacancy.id??>
                    <div class="vacancy-card">
                        <a href="/vacancies/${vacancy.id}/info" class="profile-card-link">
                        <p><strong>${vacancy.name}</strong></p>
                        <p><@spring.message code="vacancies.card.date"/> ${vacancy.updateTime}</p>
                        </a>
                    </div>
                <#else>
                    <p><@spring.message code="vacancies.empty.title"/></p>
                </#if>
            </#list>

            <#if totalVacancyPages?? && (totalVacancyPages > 1)>
                <div class="pagination">
                    <#if (currentVacancyPage > 1)>
                        <a href="/profile/viewEmployer/${userId}?page=${currentVacancyPage - 1}" class="page-link">«</a>
                    </#if>

                    <#list 1..totalVacancyPages as page>
                        <#if page == currentVacancyPage>
                            <span class="current-page">${page}</span>
                        <#else>
                            <a href="/profile/viewEmployer/${userId}?page=${page}" class="page-link">${page}</a>
                        </#if>
                    </#list>

                    <#if (currentVacancyPage < totalVacancyPages)>
                        <a href="/profile/viewEmployer/${userId}?page=${currentVacancyPage + 1}" class="page-link">»</a>
                    </#if>
                </div>
            </#if>
        </div>
    </div>
</@layout.page>