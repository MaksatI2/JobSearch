<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Личный кабинет</title>
    <link rel="stylesheet" href="/static/css/profile.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-container">
        <div class="navbar-logo">
            <a href="/">JobSearch</a>
        </div>

        <input type="checkbox" id="navbar-toggle" class="navbar-toggle">
        <label for="navbar-toggle" class="navbar-toggle-label">
            <span></span>
        </label>

        <div class="navbar-menu">
            <ul class="navbar-nav">
                <li class="nav-item"><a href="/" class="nav-link">Главная</a></li>
                <li class="nav-item"><a href="/vacancies/allVacancies" class="nav-link">Вакансии</a></li>
                <li class="nav-item"><a href="/resumes/allResumes" class="nav-link">Резюме</a></li>

                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <li class="nav-item"><a href="/profile" class="nav-link">Профиль</a></li>
                    <li class="nav-item logout">
                        <form action="/logout" method="post" class="logout-form">
                            <button type="submit" class="logout-button">Выйти</button>
                        </form>
                    </li>
                <#else>
                    <li class="nav-item"><a href="/auth/login" class="nav-link">Войти</a></li>
                    <li class="nav-item"><a href="/auth/register" class="nav-link">Регистрация</a></li>
                </#if>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <h2>Личный кабинет <#if user.accountType == "APPLICANT">Соискателя<#else>Работодателя</#if></h2>

    <div class="profile">
        <div class="left-column">
            <img src="${user.avatar?starts_with('images/')?then('/' + user.avatar, '/static/default-avatar/' + user.avatar)}"
                 alt="Аватар"
                 onerror="this.src='/static/default-avatar/default_avatar.jpg'">
            <p>${user.name} ${user.surname}</p>

            <#if user.accountType == "APPLICANT">
                <p>Возраст: ${user.age}</p>
            <#else>
                <p>Компания: ${user.name}</p>
            </#if>

            <button>Редактировать</button>
        </div>

        <div class="right-column">
            <#if user.accountType == "APPLICANT">
                <button>Создать резюме</button>

                <div class="responses-card">
                    Отклики: 2
                </div>

                <h3>Ваши резюме</h3>
                <#list resumes![] as resume>
                    <div class="resume-card">
                        <p><strong>${resume.name}</strong></p>
                        <p>Обновлено: ${resume.updateTime}</p>
                        <button>Обновить</button>
                        <button>Ред.</button>
                    </div>
                <#else>
                    <p>У вас пока нет резюме.</p>
                </#list>
            <#elseif user.accountType == "EMPLOYER">
                <button>Создать вакансию</button>

                <h3>Ваши вакансии</h3>
                <#list vacancies![] as vacancy>
                    <div class="vacancy-card">
                        <p><strong>${vacancy.name}</strong></p>
                        <p>Обновлено: ${vacancy.updateTime}</p>
                        <button>Обновить</button>
                        <button>Ред.</button>
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
