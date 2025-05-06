<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page title="Личный кабинет | JobSearch" styles="/static/css/user/profile.css" activeNav="profile">
    <h2>Личный кабинет <#if user.accountType == "APPLICANT">Соискателя<#else>Работодателя</#if></h2>

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
                     alt="Аватар">
                <a href="/profile/avatar/edit" class="change-avatar-button">Изменить аватар</a>
            </div>

            <#if user.accountType == "APPLICANT">
                <p>${user.name} ${user.surname}</p>
                <p>Email: ${user.email}</p>
                <p>Возраст:<#if user.age??>${user.age}
                    <#else>
                        <span class="text-danger">не указан</span>
                    </#if>
                </p>

                <p>Телефон: ${user.phoneNumber}</p>
            <#else>
                <p>${user.name}</p>
                <p>Email: ${user.email}</p>
                <p>Телефон: ${user.phoneNumber}</p>
            </#if>

            <a href="/profile/edit" class="edit-button">Редактировать профиль</a>
        </div>

        <div class="right-column">
            <#if user.accountType == "APPLICANT">
                <a href="/resumes/create" class="action-button">Создать резюме</a>
                <a href="/resumes/${user.id}/response" class="responses-button">
                    Отклики
                    <span class="responses-badge">${responsesCount}</span>
                </a>
                <h3>Ваши резюме</h3>
                <#list resumes![] as resume>
                    <#if resume.id??>
                        <a href="/resumes/${resume.id}/info" class="resume-card-link">
                            <div class="resume-card">
                                <p><strong>${resume.name!""}</strong></p>
                                <p>Обновлено: ${resume.updateTime!""}</p>
                                <form action="/resumes/${resume.id}/refresh" method="post" class="inline-form">
                                    <#if _csrf??>
                                        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                                               value="${(_csrf.token)!'csrf-token'}"/>
                                    </#if>
                                    <button type="submit" class="button-link refresh-button">Обновить</button>
                                </form>
                                <a href="/resumes/${resume.id}/edit" class="button-link">Редактировать</a>
                            </div>
                        </a>
                    <#else>
                        <p>Не удалось загрузить резюме.</p>
                    </#if>
                <#else>
                    <p>У вас пока нет резюме.</p>
                </#list>

                <#if totalResumePages?? && (totalResumePages > 1)>
                    <div class="pagination">
                        <#if (currentResumePage > 1)>
                            <a href="/profile?resumePage=${currentResumePage - 1}" class="page-link">«</a>
                        </#if>

                        <#list 1..totalResumePages as page>
                            <#if page == currentResumePage>
                                <span class="current-page">${page}</span>
                            <#else>
                                <a href="/profile?resumePage=${page}" class="page-link">${page}</a>
                            </#if>
                        </#list>

                        <#if (currentResumePage < totalResumePages)>
                            <a href="/profile?resumePage=${currentResumePage + 1}" class="page-link">»</a>
                        </#if>
                    </div>
                </#if>

            <#elseif user.accountType == "EMPLOYER">
                <a href="/vacancies/create" class="action-button">Создать вакансию</a>
                <a href="/vacancies/${user.id}/response" class="responses-button">
                    Отклики
                    <span class="responses-badge">${responsesCount}</span>
                </a>
                <h3>Ваши вакансии</h3>
                <#list vacancies![] as vacancy>
                    <#if vacancy.id??>
                        <a href="/vacancies/${vacancy.id}/info" class="vacancy-card-link">
                            <div class="vacancy-card">
                                <p><strong>${vacancy.name}</strong></p>
                                <p>Обновлено: ${vacancy.updateTime}</p>
                                <form action="/vacancies/${vacancy.id}/refresh" method="post" class="inline-form">
                                    <#if _csrf??>
                                        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                                               value="${(_csrf.token)!'csrf-token'}"/>
                                    </#if>
                                    <button type="submit" class="button-link refresh-button">Обновить</button>
                                </form>
                                <a href="/vacancies/${vacancy.id}/edit" class="button-link">Редактировать</a>
                            </div>
                        </a>
                    <#else>
                        <p>Не удалось загрузить вакансию.</p>
                    </#if>
                <#else>
                    <p>У вас пока нет вакансий.</p>
                </#list>

                <#if totalVacancyPages?? && (totalVacancyPages > 1)>
                    <div class="pagination">
                        <#if (currentVacancyPage > 1)>
                            <a href="/profile?vacancyPage=${currentVacancyPage - 1}" class="page-link">«</a>
                        </#if>

                        <#list 1..totalVacancyPages as page>
                            <#if page == currentVacancyPage>
                                <span class="current-page">${page}</span>
                            <#else>
                                <a href="/profile?vacancyPage=${page}" class="page-link">${page}</a>
                            </#if>
                        </#list>

                        <#if (currentVacancyPage < totalVacancyPages)>
                            <a href="/profile?vacancyPage=${currentVacancyPage + 1}" class="page-link">»</a>
                        </#if>
                    </div>
                </#if>
            </#if>
        </div>
    </div>
</@layout.page>