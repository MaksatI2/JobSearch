<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход | JobSearch</title>
    <link rel="stylesheet" href="/static/css/auth/register.css">
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
                <#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <a href="/resumes/allResumes" class="nav-link">Резюме</a>
                </#if>
                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <a href="/profile" class="nav-link">Профиль</a>
                    <form action="/logout" method="post" class="logout-form">
                        <button type="submit" class="logout-btn">Выйти</button>
                    </form>
                <#else>
                    <a href="/auth/login" class="nav-link active">Войти</a>
                    <a href="/auth/register" class="nav-link register-btn">Регистрация</a>
                </#if>
            </div>
        </nav>
    </div>
</header>

<main class="main">
    <div class="container">
        <div class="registration-form-container">
            <h1 class="page-title">Вход в систему</h1>

            <#if loginError??>
                <div class="global-error">
                    ${loginError}
                </div>
            </#if>

            <#if registrationSuccess??>
                <div class="success-message">
                    ${registrationSuccess}
                </div>
            </#if>

            <form action="/login" method="post" class="registration-form">
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="username" class="form-control" required>
                </div>

                <div class="form-group">
                    <label for="password">Пароль</label>
                    <input type="password" id="password" name="password" class="form-control" required>
                </div>

                <button type="submit" class="submit-btn">Войти</button>
            </form>

            <div class="login-link">
                Ещё нет аккаунта? <a href="/auth/register">Зарегистрироваться</a>
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