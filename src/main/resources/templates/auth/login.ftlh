<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход | JobSearch</title>
    <link rel="stylesheet" href="/static/css/register.css">
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

            <div class="register-link">
                Ещё нет аккаунта? <a href="/auth/register">Зарегистрироваться</a>
            </div>
        </div>
    </div>
</main>
</body>
</html>