<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Избранные вакансии | JobSearch</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/vacancies/vacancies.css">
    <link rel="stylesheet" href="/static/css/header.css">
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
            <a href="/vacancies" class="nav-link">Вакансии</a>
            <a href="/company/allCompanies" class="nav-link">Компании</a>
            <a href="/profile" class="nav-link active">Профиль</a>
            <form action="/logout" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="logout-btn" id="logout">Выйти</button>
            </form>
        </div>
    </nav>
</header>

<main class="main">
    <div class="container">
        <h1 class="page-title">Избранные вакансии</h1>
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
        <div class="favorite-container">
            <a href="/vacancies" class="submit-btn">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" class="btn-icon">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                </svg>
                Вернуться к вакансиям
            </a>
        </div>

        <#if vacancies?size == 0>
            <div class="empty-state">
                <h3>Нет избранных вакансий</h3>
                <p>Вы еще не добавили ни одной вакансии в избранное.</p>
                <a href="/vacancies" class="submit-btn">Посмотреть вакансии</a>
            </div>
        <#else>
            <div class="vacancies-grid">
                <#list vacancies as vacancy>
                    <div class="vacancy-card">
                        <a href="/vacancies/${vacancy.id}/info" class="vacancy-card-link">
                            <div class="vacancy-header">
                                <h2 class="vacancy-title">${vacancy.name}</h2>
                                <div class="salary">${vacancy.salary} с</div>
                            </div>
                            <p class="vacancy-description">${vacancy.description}</p>
                            <div class="vacancy-meta">
                                <span class="experience">
                                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                                    </svg>
                                    Опыт: <span>${vacancy.expFrom}-${vacancy.expTo} лет</span>
                                </span>
                                <span class="author">
                                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                                    </svg>
                                    Автор: <span>${vacancy.authorName}</span>
                                </span>
                                <span class="update-time">
                                <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                </svg>
                                Обновлена: ${vacancy.updateTime?string("dd.MM.yyyy")}
                            </span>
                            </div>
                        </a>
                        <div class="favorite-actions">
                            <form action="/vacancies/${vacancy.id}/unFavorite" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="remove-favorite-btn">
                                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                                    </svg>
                                    Удалить из избранного
                                </button>
                            </form>
                        </div>
                    </div>
                </#list>
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