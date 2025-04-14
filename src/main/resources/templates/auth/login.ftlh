<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход в систему</title>
    <link rel="stylesheet" href="/static/css/login.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
<header class="header">
    <div class="container">
        <nav class="navbar">
            <a href="/" class="logo">JobSearch</a>
            <div class="nav-links">
                <a href="/" class="nav-link">Главная</a>
                <a href="/vacancies" class="nav-link">Вакансии</a>
            </div>
        </nav>
    </div>
</header>
<div class="login-container">
    <h2>Вход в систему</h2>

    <#if loginError??>
        <div class="error-message">
            ${loginError}
        </div>
    </#if>

    <#if registrationSuccess??>
        <div class="success-message">
            ${registrationSuccess}
        </div>
    </#if>

    <form action="/login" method="post">
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="username" required>
        </div>

        <div class="form-group">
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" required>
        </div>

        <button type="submit">Войти</button>
    </form>

    <div class="register-link">
        Ещё нет аккаунта? <a href="/auth/register">Зарегистрироваться</a>
    </div>
</div>
</body>
</html>