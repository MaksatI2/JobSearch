<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Активные вакансии</title>
    <link rel="stylesheet" href="/static/css/vacancies.css">
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
                <li><a href="/">Вакансии</a></li>
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
    <h1>Список активных вакансий</h1>

    <#if vacancies?? && vacancies?has_content>
        <div class="vacancies-list">
            <#list vacancies as vacancy>
                <div class="vacancy-card">
                    <h2>${vacancy.name}</h2>
                    <p>${vacancy.description}</p>
                    <p class="salary">Зарплата: ${vacancy.salary} руб.</p>
                    <p><span class="experience">Опыт: от ${vacancy.expFrom} до ${vacancy.expTo} лет</span></p>
                    <p class="vacancy-meta">Дата обновления: ${vacancy.updateTime?string("dd.MM.yyyy HH:mm")}</p>
                </div>
            </#list>
        </div>
    <#else>
        <div class="no-vacancies">
            <p>Нет доступных вакансий.</p>
        </div>
    </#if>
</div>
</body>
</html>