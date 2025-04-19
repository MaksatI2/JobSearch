<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Детали вакансии | JobSearch</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/vacancies/vacancyInfo.css">
    <link rel="icon" href="/static/images/logo-transparent.png" type="image/x-icon">
</head>
<body>
<header class="header">
    <nav class="navbar">
        <a href="/" class="logo">
            <img src="/static/images/logo-transparent.png" alt="JobSearch" class="logo-img">
        </a>
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
</header>

<main class="main">
    <div class="container">
        <div class="vacancy-detail">
            <div class="vacancy-header">
                <h1 class="vacancy-title">${vacancy.name}</h1>
                <div class="salary">${vacancy.salary} с</div>
            </div>

            <div class="vacancy-meta">
                <span class="experience">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    Опыт: ${vacancy.expFrom}-${vacancy.expTo} лет
                </span>
                <span class="author">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                    Автор: ${vacancy.authorName?default("Неизвестно")}
                </span>
                <span class="update-time">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                    </svg>
                    Обновлена: ${vacancy.updateTime?string("dd.MM.yyyy")}
                </span>
                <span class="create-time">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    Опубликована: ${vacancy.createDate?string("dd.MM.yyyy")}
                </span>
            </div>

            <div class="section">
                <h2 class="section-title">Описание вакансии</h2>
                <div class="section-content">${vacancy.description}</div>
            </div>

            <div class="section">
                <h2 class="section-title">Требования</h2>
                <div class="section-content">
                    <p>Опыт работы: от ${vacancy.expFrom} до ${vacancy.expTo} лет</p>
                </div>
            </div>

            <div class="actions">
                <a href="/vacancies" class="btn btn-back">Назад к списку</a>
                <#if isAuthor>
                    <a href="/vacancies/${vacancy.id}/edit" class="btn btn-edit">Редактировать</a>
                    <form action="/vacancies/${vacancy.id}/refresh" method="post">
                        <button type="submit" class="btn btn-refresh">Обновить</button>
                    </form>
                </#if>
            </div>
        </div>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>© 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>