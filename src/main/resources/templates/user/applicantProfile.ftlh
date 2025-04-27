<#import "../layout.ftlh" as layout>

<@layout.page title="Личный кабинет соискателя | JobSearch" styles="/static/css/user/profile.css" activeNav="profile">
    <h2>Личный кабинет Соискателя</h2>

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
                <img src="${user.avatar}" alt="Аватар" onerror="this.src='/static/default-avatar/default_avatar.jpg'">
            </div>
            <p>${user.name} ${user.surname}</p>
            <p>Email: ${user.email}</p>
            <p>Возраст: ${user.age}</p>
            <p>Телефон: ${user.phoneNumber}</p>
        </div>

        <div class="right-column">
            <h3>Резюме соискателя</h3>
            <#list resumes![] as resume>
                <#if resume.id??>
                    <div class="resume-card">
                        <p><strong>${resume.name!""}</strong></p>
                        <p>Обновлено: ${resume.updateTime!""}</p>
                        <a href="/resumes/${resume.id}/info" class="button-link">Подробнее</a>
                    </div>
                <#else>
                    <p>Не удалось загрузить резюме.</p>
                </#if>
            <#else>
                <p>У вас пока нет резюме.</p>
            </#list>

            <#if totalResumePages?? && (totalResumePages > 1)>
                <div class="pagination">
                    <#if (currentResumePage > 1)>
                        <a href="/profile/viewApplicant/${userId}?page=${currentResumePage - 1}" class="page-link">Предыдущая</a>
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