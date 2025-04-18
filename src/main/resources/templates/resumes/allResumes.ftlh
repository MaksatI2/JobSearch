<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Активные резюме | JobSearch</title>
    <link rel="stylesheet" href="/static/css/resumes/resumes.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="icon" href="/static/images/logo-transparent.png" type="image/x-icon">
</head>
<body>
<header class="header">
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
</header>

<main class="main">
    <div class="container">
        <h1 class="page-title">Список активных резюме</h1>

        <#if resumes?? && resumes?has_content>
            <div class="resumes-grid">
                <#list resumes as resume>
                    <article class="resume-card">
                        <div class="resume-header">
                            <h2 class="resume-title">${resume.name}</h2>
                            <span class="salary">${resume.salary} с</span>
                        </div>

                        <#if resume.workExperiences?? && resume.workExperiences?has_content>
                            <div class="work-experience">
                                <h3 class="section-title">Опыт работы</h3>
                                <#list resume.workExperiences as experience>
                                    <div class="experience-item">
                                        <p class="experience-title"><strong>${experience.companyName}</strong> — ${experience.position}</p>
                                        <p class="experience-details">Стаж: ${experience.years} лет</p>
                                        <p class="experience-details">Обязанности: ${experience.responsibilities}</p>
                                    </div>
                                </#list>
                            </div>
                        </#if>

                        <#if resume.educationInfos?? && resume.educationInfos?has_content>
                            <div class="education">
                                <h3 class="section-title">Образование</h3>
                                <#list resume.educationInfos as education>
                                    <div class="education-item">
                                        <p class="education-title"><strong>${education.institution}</strong> — ${education.program}</p>
                                        <p class="education-details">Степень: ${education.degree}</p>
                                        <p class="education-details">Период: ${education.startDate?string("dd.MM.yyyy")} – ${education.endDate?string("dd.MM.yyyy")}</p>
                                    </div>
                                </#list>
                            </div>
                        </#if>

                        <div class="resume-meta">
                            <span class="update-time">
                                <svg class="icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                </svg>
                                ${resume.updateTime?string("dd.MM.yyyy")}
                            </span>
                        </div>
                    </article>
                </#list>
            </div>
        <#else>
            <div class="empty-state">
                <h3>Резюме не найдены</h3>
                <p>Попробуйте зайти позже</p>
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