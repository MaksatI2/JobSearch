<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/user/profile.css" activeNav="profile">
    <h2><@spring.message code="profile.title"/>
        <@spring.message code="profile.applicant.title"/></h2>
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
                <img src="/api/users/${user.id}/avatar" alt="Аватар" onerror="this.src='/static/default-avatar/default_avatar.jpg'">
            </div>
            <p>${user.name} ${user.surname}</p>
            <p>Email: ${user.email}</p>
            <p><@spring.message code="profile.applicant.age"/><#if user.age??>${user.age}
            <#else>
                <span class="text-danger"><@spring.message code="profile.empty.user.info"/> </span>
                </#if>
            </p>
            <p><@spring.message code="profile.phone"/> ${user.phoneNumber}</p>
        </div>

        <div class="right-column">
            <h3><@spring.message code="applicant.resumes.title"/></h3>
            <#list resumes![] as resume>
                <#if resume.id??>
                    <div class="resume-card">
                        <a href="/resumes/${resume.id}/info" class="profile-card-link">
                        <p><strong>${resume.name!""}</strong></p>
                        <p><@spring.message code="resumes.date"/> ${resume.updateTime!""}</p>
                        </a>
                    </div>
                <#else>
                    <p><@spring.message code="profile.applicant.resumes.empty"/></p>
                </#if>
            </#list>

            <#if totalResumePages?? && (totalResumePages > 1)>
                <div class="pagination">
                    <#if (currentResumePage > 1)>
                        <a href="/profile/viewApplicant/${userId}?page=${currentResumePage - 1}" class="page-link">«</a>
                    </#if>
                    <#list 1..totalResumePages as page>
                        <#if page == currentResumePage>
                            <span class="current-page">${page}</span>
                        <#else>
                            <a href="/profile/viewApplicant/${userId}?page=${page}" class="page-link">${page}</a>
                        </#if>
                    </#list>
                    <#if (currentResumePage < totalResumePages)>
                        <a href="/profile/viewApplicant/${userId}?page=${currentResumePage + 1}" class="page-link">»</a>
                    </#if>
                </div>
            </#if>
        </div>
    </div>
</@layout.page>