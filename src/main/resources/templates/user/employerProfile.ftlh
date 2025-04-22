<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Личный кабинет работодателя | JobSearch</title>
    <link rel="stylesheet" href="/static/css/user/profile.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="icon" href="/static/images/logo-transparent.png" type="image/x-icon">
</head>
<body>
<header class="header">
    <div class="container">
        <nav class="navbar">
            <img src="/static/images/logo-transparent.png" alt="JobSearch" class="logo-img">
            <div class="nav-links">
                <a href="/" class="nav-link">Главная</a>
                <a href="/vacancies" class="nav-link">Вакансии</a>
                <a href="/company/allCompanies" class="nav-link active">Компании</a>
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
        <h2>Личный кабинет Работодателя</h2>

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
                </div>

                <p>${user.name}</p>
                <p>Email: ${user.email}</p>
                <p>Телефон: ${user.phoneNumber}</p>
            </div>

            <div class="right-column">
                <h3>Вакансии компании</h3>
                <#list vacancies![] as vacancy>
                    <#if vacancy.id??>
                        <div class="vacancy-card">
                            <p><strong>${vacancy.name}</strong></p>
                            <p>Обновлено: ${vacancy.updateTime}</p>
                            <a href="/vacancies/${vacancy.id}/info" class="button-link">Подробнее</a>
                        </div>
                    <#else>
                        <p>Не удалось загрузить вакансию.</p>
                    </#if>
                <#else>
                    <p>У вас пока нет вакансий.</p>
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
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>© 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>