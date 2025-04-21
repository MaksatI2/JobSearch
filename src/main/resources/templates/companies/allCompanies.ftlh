<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Компании | JobSearch</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="icon" href="/static/images/logo-transparent.png" type="image/x-icon">
    <link rel="stylesheet" href="/static/css/companies/allCompanies.css">
</head>
<body>
<header class="header">
    <nav class="navbar">
        <img src="/static/images/logo-transparent.png" alt="JobSearch" class="logo-img">
        <div class="nav-links">
            <a href="/" class="nav-link">Главная</a>
            <a href="/vacancies" class="nav-link">Вакансии</a>
            <a href="/resumes/allResumes" class="nav-link">Резюме</a>
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
</header>

<main class="main">
    <div class="container">
        <h1>Список компаний</h1>

        <#if companies?? && companies?size gt 0>
            <div class="company-list">
                <#list companies as company>
                    <div class="company-card">
                        <div class="company-header">
                            <img src="${company.avatar}" alt="Company Logo" class="company-avatar">
                            <h3 class="company-name">${company.name}</h3>
                        </div>
                        <div class="company-contacts">
                            <div class="contact-item">
                                <span>✉️</span>
                                <span>${company.email}</span>
                            </div>
                            <div class="contact-item">
                                <span>📞</span>
                                <span>${company.phoneNumber}</span>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="no-companies">
                <p>На данный момент нет зарегистрированных компаний</p>
            </div>
        </#if>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>© 2023 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>