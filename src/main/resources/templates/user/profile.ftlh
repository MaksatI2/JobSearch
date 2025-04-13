<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Личный кабинет | JobSearch</title>
    <link rel="stylesheet" href="/static/css/profile.css">
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
        <h2>Личный кабинет <#if user.accountType == "APPLICANT">Соискателя<#else>Работодателя</#if></h2>

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
                    <a href="#" class="change-avatar-button">Изменить аватар</a>
                </div>

                <#if user.accountType == "APPLICANT">
                    <p>${user.name} ${user.surname}</p>
                    <p>Email: ${user.email}</p>
                    <p>Возраст: ${user.age}</p>
                    <p>Телефон: ${user.phoneNumber}</p>
                <#else>
                    <p>${user.name}</p>
                    <p>Телефон: ${user.phoneNumber}</p>
                </#if>

                <a href="/profile/edit" class="edit-button">Редактировать профиль</a>
            </div>

            <div class="right-column">
                <#if user.accountType == "APPLICANT">
                    <a href="/resumes/create" class="action-button">Создать резюме</a>

                    <div class="responses-card">
                        Отклики: ${responsesCount}
                    </div>

                    <h3>Ваши резюме</h3>
                    <#list resumes![] as resume>
                        <div class="resume-card">
                            <p><strong>${resume.name}</strong></p>
                            <p>Обновлено: ${resume.updateTime}</p>
                            <a href="#" class="button-link">Обновить</a>
                            <a href="#" class="button-link">Редактировать</a>
                        </div>
                    <#else>
                        <p>У вас пока нет резюме.</p>
                    </#list>
                <#elseif user.accountType == "EMPLOYER">
                    <a href="/vacancies/create" class="action-button">Создать вакансию</a>

                    <h3>Ваши вакансии</h3>
                    <#list vacancies![] as vacancy>
                        <#if vacancy.id??>
                            <div class="vacancy-card">
                                <p><strong>${vacancy.name}</strong></p>
                                <p>Обновлено: ${vacancy.updateTime}</p>
                                <a href="/vacancies/${vacancy.id}/edit" class="button-link">Редактировать</a>
                            </div>
                        <#else>
                            <p>Не удалось загрузить вакансию.</p>
                        </#if>
                    <#else>
                        <p>У вас пока нет вакансий.</p>
                    </#list>
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