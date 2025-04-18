<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вакансии | JobSearch</title>
    <link rel="stylesheet" href="/static/css/vacancies.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
<header class="header">
    <div class="container">
        <nav class="navbar">
            <a href="/" class="logo">JobSearch</a>
            <div class="nav-links">
                <a href="/" class="nav-link">Главная</a>
                <a href="/vacancies" class="nav-link active">Вакансии</a>
                <#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <a href="/resumes/allResumes" class="nav-link">Резюме</a>
                </#if>
                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <a href="/profile" class="nav-link">Профиль</a>
                    <form action="/logout" method="post" class="logout-form">
                        <button type="submit" class="logout-btn">Выйти</button>
                    </form>
                <#else>
                    <a href="/auth/login" class="nav-link">Войти</a>
                    <a href="/auth/register" class="nav-link register-btn">Регистрация</a>
                </#if>
            </div>
        </nav>
    </div>
</header>

<main class="main">
    <div class="container">
        <h1 class="page-title">Все вакансии</h1>

        <#if vacancies?? && vacancies?has_content>
            <div class="vacancies-grid">
                <#list vacancies as vacancy>
                    <article class="vacancy-card">
                        <div class="vacancy-header">
                            <h2 class="vacancy-title">${vacancy.name}</h2>
                            <span class="salary">${vacancy.salary} с</span>
                        </div>
                        <p class="vacancy-description">${vacancy.description}</p>
                        <div class="vacancy-meta">
                            <span class="experience">
                                <svg class="icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                                </svg>
                                Опыт: ${vacancy.expFrom}-${vacancy.expTo} лет
                            </span>
                            <span class="update-time">
                                <svg class="icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                </svg>
                                ${vacancy.updateTime?string("dd.MM.yyyy")}
                            </span>
                        </div>
                    </article>
                </#list>
            </div>
        <#else>
            <div class="empty-state">
                <h3>Вакансии не найдены</h3>
                <p>Попробуйте зайти позже</p>
                <a href="/vacancies" class="details-btn">Вернуться ко всем вакансиям</a>
            </div>
        </#if>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>© 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>