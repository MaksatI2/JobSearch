<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Личный кабинет | JobSearch</title>
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
                <#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <a href="/resumes/allResumes" class="nav-link">Резюме</a>
                <#else>
                    <a href="/company/allCompanies" class="nav-link">Компании</a>
                </#if>
                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <a href="/profile" class="nav-link active">Профиль</a>
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
                    <a href="/profile/avatar/edit" class="change-avatar-button">Изменить аватар</a>
                </div>

                <#if user.accountType == "APPLICANT">
                    <p>${user.name} ${user.surname}</p>
                    <p>Email: ${user.email}</p>
                    <p>Возраст: ${user.age}</p>
                    <p>Телефон: ${user.phoneNumber}</p>
                <#else>
                    <p>${user.name}</p>
                    <p>Email: ${user.email}</p>
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
                     <#if resume.id??>
                        <div class="resume-card">
                            <p><strong>${resume.name!""}</strong></p>
                              <p>Обновлено: ${resume.updateTime!""}</p>
                             <form action="/resumes/${resume.id}/refresh" method="post" class="inline-form">
                                  <button type="submit" class="button-link refresh-button">Обновить</button>
                             </form>
                                   <a href="/resumes/${resume.id}/edit" class="button-link">Редактировать</a>
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
                                <a href="/profile?resumePage=${currentResumePage - 1}" class="page-link">Предыдущая</a>
                            </#if>

                            <#list 1..totalResumePages as page>
                                <#if page == currentResumePage>
                                    <span class="current-page">${page}</span>
                                <#else>
                                    <a href="/profile?resumePage=${page}" class="page-link">${page}</a>
                                </#if>
                            </#list>

                            <#if (currentResumePage < totalResumePages)>
                                <a href="/profile?resumePage=${currentResumePage + 1}" class="page-link">Следующая</a>
                            </#if>
                        </div>
                    </#if>

                <#elseif user.accountType == "EMPLOYER">
                    <a href="/vacancies/create" class="action-button">Создать вакансию</a>

                    <h3>Ваши вакансии</h3>
                    <#list vacancies![] as vacancy>
                        <#if vacancy.id??>
                            <div class="vacancy-card">
                                <p><strong>${vacancy.name}</strong></p>
                                <p>Обновлено: ${vacancy.updateTime}</p>
                                <form action="/vacancies/${vacancy.id}/refresh" method="post" class="inline-form">
                                    <button type="submit" class="button-link refresh-button">Обновить</button>
                                </form>
                                <a href="/vacancies/${vacancy.id}/edit" class="button-link">Редактировать</a>
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
                                <a href="/profile?vacancyPage=${currentVacancyPage - 1}" class="page-link">Предыдущая</a>
                            </#if>

                            <#list 1..totalVacancyPages as page>
                                <#if page == currentVacancyPage>
                                    <span class="current-page">${page}</span>
                                <#else>
                                    <a href="/profile?vacancyPage=${page}" class="page-link">${page}</a>
                                </#if>
                            </#list>

                            <#if (currentVacancyPage < totalVacancyPages)>
                                <a href="/profile?vacancyPage=${currentVacancyPage + 1}" class="page-link">Следующая</a>
                            </#if>
                        </div>
                    </#if>
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