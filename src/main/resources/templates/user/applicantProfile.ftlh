<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Личный кабинет соискателя | JobSearch</title>
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
                <a href="/resumes/allResumes" class="nav-link active">Резюме</a>
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
        <h2>Личный кабинет Соискателя</h2>

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

                <p>${user.name} ${user.surname}</p>
                <p>Email: ${user.email}</p>
                <p>Возраст: ${user.age}</p>
                <p>Телефон: ${user.phoneNumber}</p>
            </div>

            <div class="right-column">
                <h3>Резюме соискателя</h3>
                <#list resumes![] as resume>
                    <#if resume.id??>
                        <div class="resume-card">
                            <p><strong>${resume.name!""}</strong></p>
                            <p>Обновлено: ${resume.updateTime!""}</p>
                            <a href="/resumes/${resume.id}/info" class="button-link">Подробнее</a>
                        </div>
                    <#else>
                        <p>Не удалось загрузить резюме.</p>
                    </#if>
                <#else>
                    <p>У вас пока нет резюме.</p>
                </#list>

                <#if totalResumePages?? && (totalResumePages > 1)>
                    <div class="pagination">
                        <#if (currentResumePage > 1)>
                            <a href="/profile/viewApplicant/${userId}?page=${currentResumePage - 1}" class="page-link">Предыдущая</a>
                        </#if>

                        <#list 1..totalResumePages as page>
                            <#if page == currentResumePage>
                                <span class="current-page">${page}</span>
                            <#else>
                                <a href="/profile/viewApplicant/${userId}?page=${page}" class="page-link">${page}</a>
                            </#if>
                        </#list>

                        <#if (currentResumePage < totalResumePages)>
                            <a href="/profile/viewApplicant/${userId}?page=${currentResumePage + 1}" class="page-link">»</a>
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