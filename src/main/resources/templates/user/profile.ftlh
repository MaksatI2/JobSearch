<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Личный кабинет | JobSearch</title>
    <link rel="stylesheet" href="/static/css/profile.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-container">
        <div class="navbar-logo">
            <a href="/">JobSearch</a>
        </div>

        <div class="navbar-menu">
            <ul>
                <li><a href="/">Главная</a></li>
                <li><a href="/vacancies">Вакансии</a></li>
                <#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <li><a href="/resumes/allResumes">Резюме</a></li>
                </#if>

                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <li><a href="/profile">Профиль</a></li>
                    <li>
                        <form action="/logout" method="post" style="display: inline;">
                            <button type="submit" class="logout-button">Выйти</button>
                        </form>
                    </li>
                <#else>
                    <li><a href="/auth/login">Войти</a></li>
                    <li><a href="/auth/register">Регистрация</a></li>
                </#if>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
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
                <img src="${user.avatar}"
                     alt="Аватар"
                     onerror="this.src='/static/default-avatar/default_avatar.jpg'">
                <a href="#" class="change-avatar-button">Изменить аватар</a>
            </div>

            <#if user.accountType == "APPLICANT">
                <p>${user.name} ${user.surname}</p>
                <p>Email: ${user.email}</p>
                <p>Возраст: ${user.age}</p>
                <p>Телефон: ${user.phoneNumber}</p>
            <#else>
                <p>${user.name}</p>
                <p>Телефон: ${user.phoneNumber}</p>
            </#if>

            <a href="/profile/edit" class="edit-button">Редактировать профиль</a>
        </div>

        <div class="right-column">
            <#if user.accountType == "APPLICANT">
                <a href="#" class="action-button">Создать резюме</a>

                <div class="responses-card">
                    Отклики: ${responsesCount}
                </div>

                <h3>Ваши резюме</h3>
                <#list resumes![] as resume>
                    <div class="resume-card">
                        <p><strong>${resume.name}</strong></p>
                        <p>Обновлено: ${resume.updateTime}</p>
                        <a href="#" class="button-link">Обновить</a>
                        <a href="#" class="button-link">Редактировать</a>
                    </div>
                <#else>
                    <p>У вас пока нет резюме.</p>
                </#list>
            <#elseif user.accountType == "EMPLOYER">
                <a href="/vacancies/create" class="action-button">Создать вакансию</a>

                <h3>Ваши вакансии</h3>
                <#list vacancies![] as vacancy>
                    <div class="vacancy-card">
                        <p><strong>${vacancy.name}</strong></p>
                        <p>Обновлено: ${vacancy.updateTime}</p>
                        <a href="#" class="button-link">Обновить</a>
                        <a href="#" class="button-link">Редактировать</a>
                    </div>
                <#else>
                    <p>У вас пока нет вакансий.</p>
                </#list>
            </#if>
        </div>
    </div>
</div>
</body>
</html>