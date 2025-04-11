<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Активные резюме</title>
    <link rel="stylesheet" href="/static/css/resumes.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-container">
        <div class="navbar-logo">
            <a href="/">JobSearch</a>
        </div>

        <div class="navbar-menu">
            <ul>
                <li><a href="/">Главная</a></li>
                <li><a href="/">Вакансии</a></li>
                <li><a href="/resumes/allResumes">Резюме</a></li>

                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <li><a href="/profile">Профиль</a></li>
                    <li>
                        <form action="/logout" method="post" style="display: inline;">
                            <button type="submit" class="logout-button">Выйти</button>
                        </form>
                    </li>
                <#else>
                    <li><a href="/auth/login">Войти</a></li>
                    <li><a href="/auth/register">Регистрация</a></li>
                </#if>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <h1>Список активных резюме</h1>

    <#if resumes?? && resumes?has_content>
        <div class="resumes-list">
            <#list resumes as resume>
                <div class="resume-card">
                    <h2>${resume.name}</h2>
                    <p class="salary">Ожидаемая зарплата: ${resume.salary} руб.</p>

                    <#if resume.workExperiences?? && resume.workExperiences?has_content>
                        <div class="work-experience">
                            <h3>Опыт работы:</h3>
                            <#list resume.workExperiences as experience>
                                <div class="experience-item">
                                    <p><strong>${experience.companyName}</strong> - ${experience.position}</p>
                                    <p>Стаж: ${experience.years} лет</p>
                                    <p>Обязанности: ${experience.responsibilities}</p>
                                </div>
                            </#list>
                        </div>
                    </#if>

                    <#if resume.educationInfos?? && resume.educationInfos?has_content>
                        <div class="education">
                            <h3>Образование:</h3>
                            <#list resume.educationInfos as education>
                                <div class="education-item">
                                    <p><strong>${education.institution}</strong> - ${education.program}</p>
                                    <p>Степень: ${education.degree}</p>
                                    <p>Период обучения: ${education.startDate?string("dd.MM.yyyy")} - ${education.endDate?string("dd.MM.yyyy")}</p>
                                </div>
                            </#list>
                        </div>
                    </#if>

                    <p class="resume-meta">Дата обновления: ${resume.updateTime?string("dd.MM.yyyy HH:mm")}</p>
                </div>
            </#list>
        </div>
    <#else>
        <div class="no-resumes">
            <p>Нет доступных резюме.</p>
        </div>
    </#if>
</div>
</body>
</html>