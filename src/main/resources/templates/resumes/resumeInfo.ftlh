<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Детали резюме | JobSearch</title>
    <link rel="stylesheet" href="/static/css/resumes/resumeInfo.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="icon" href="/static/images/logo-transparent.png" type="image/x-icon">
</head>
<body>
<header class="header">
    <nav class="navbar">
        <a href="/" class="logo">
            <img src="/static/images/logo-transparent.png" alt="JobSearch" class="logo-img">
        </a>
        <div class="nav-links">
            <a href="/" class="nav-link">Главная</a>
            <a href="/vacancies" class="nav-link">Вакансии</a>
            <#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                <a href="/resumes/allResumes" class="nav-link active">Резюме</a>
            </#if>
            <#if Session.SPRING_SECURITY_CONTEXT??>
                <a href="/profile" class="nav-link">Профиль</a>
                <#if _csrf??>
                    <form action="/logout" method="post">
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf'}"
                               value="${(_csrf.token)!'--no-token--'}"/>
                        <button type="submit" class="logout-btn" id="logout">Выйти</button>
                    </form>
                </#if>
            <#else>
                <a href="/auth/login" class="nav-link">Войти</a>
                <a href="/auth/register" class="nav-link register-btn">Регистрация</a>
            </#if>
        </div>
    </nav>
</header>

<main class="main">
    <div class="container">
        <div class="resume-detail">
            <div class="resume-header">
                <div class="resume-title-area">
                    <h1 class="resume-title">${resume.name}</h1>
                    <div class="applicant-name">
                        <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                        </svg>
                        ${resume.applicantName?default("Неизвестно")}
                    </div>
                </div>
                <div class="salary-area">
                    <div class="salary-label">Ожидаемая зарплата</div>
                    <div class="salary">${resume.salary} с</div>
                </div>
            </div>

            <div class="resume-meta">
                <span class="update-time">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    Обновлено: ${resume.updateTime?string("dd.MM.yyyy")}
                </span>
                <span class="create-time">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                    </svg>
                    Создано: ${resume.createDate?string("dd.MM.yyyy")}
                </span>
            </div>

            <div class="section">
                <h2 class="section-title">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                         stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
                        <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path>
                    </svg>
                    Опыт работы
                </h2>
                <div class="section-content">
                    <#if resume.workExperiences?? && resume.workExperiences?size gt 0>
                        <#list resume.workExperiences as exp>
                            <div class="experience-item">
                                <div class="item-header">
                                    <div>
                                        <div class="company-name">${exp.companyName}</div>
                                        <div class="position">${exp.position}</div>
                                    </div>
                                    <div class="years">Опыт: ${exp.years} года </div>
                                </div>
                                <div class="responsibilities">${exp.responsibilities}</div>
                            </div>
                        </#list>
                    <#else>
                        <div class="empty-message">Опыт работы не указан</div>
                    </#if>
                </div>
            </div>

            <div class="section">
                <h2 class="section-title">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                         stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M22 10v6M2 10l10-5 10 5-10 5z"></path>
                        <path d="M6 12v5c3 3 9 3 12 0v-5"></path>
                    </svg>
                    Образование
                </h2>
                <div class="section-content">
                    <#if resume.educationInfos?? && resume.educationInfos?size gt 0>
                        <#list resume.educationInfos as edu>
                            <div class="education-item">
                                <div class="item-header">
                                    <div>
                                        <div class="institution-name">${edu.institution}</div>
                                        <div class="degree">${edu.degree}</div>
                                        <div class="program">${edu.program}</div>
                                    </div>
                                    <div class="date-range">
                                        ${edu.startDate?string("yyyy")} - ${edu.endDate?string("yyyy")}
                                    </div>
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <div class="empty-message">Образование не указано</div>
                    </#if>
                </div>
            </div>

            <div class="section">
                <h2 class="section-title">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                         stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M22 16.92v3a2 2 0 01-2.18 2 19.79 19.79 0 01-8.63-3.07 19.5 19.5 0 01-6-6 19.79 19.79 0 01-3.07-8.67A2 2 0 014.11 2h3a2 2 0 012 1.72 12.84 12.84 0 00.7 2.81 2 2 0 01-.45 2.11L8.09 9.91a16 16 0 006 6l1.27-1.27a2 2 0 012.11-.45 12.84 12.84 0 002.81.7A2 2 0 0122 16.92z"></path>
                    </svg>
                    Контакты
                </h2>
                <div class="section-content">
                    <#if resume.contactInfos?? && resume.contactInfos?size gt 0>
                        <div class="contacts-list">
                            <#list resume.contactInfos as contact>
                                <div class="contact-item">
                                    <div class="contact-type">${contact.getTypeName()}:</div>
                                    <div class="contact-value">${contact.value}</div>
                                </div>
                            </#list>
                        </div>
                    <#else>
                        <div class="empty-message">Контактная информация не указана</div>
                    </#if>
                </div>
            </div>

            <div class="actions">
                <#if isApplicant>
                    <a href="/profile" class="btn btn-back">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                             stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M19 12H5M12 19l-7-7 7-7"></path>
                        </svg>
                        Назад к профилю
                    </a>
                <#else>
                    <a href="/resumes/allResumes" class="btn btn-back">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                             stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M19 12H5M12 19l-7-7 7-7"></path>
                        </svg>
                        Назад к списку
                    </a>
                </#if>
                <#if isApplicant>
                    <a href="/resumes/${resume.id}/edit" class="btn btn-edit">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                             stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"></path>
                            <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                        </svg>
                        Редактировать
                    </a>
                </#if>
                <#if isApplicant>
                    <div class="right-actions">
                        <form action="/resumes/${resume.id}/delete" method="post">
                            <button type="submit" class="btn btn-delete">Удалить</button>
                        </form>
                    </div>
                </#if>
                <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('APPLICANT')>
                    <#if resume.applicantId??>
                        <a href="/profile/viewApplicant/${resume.applicantId}" class="applicant-profile-btn">
                            Профиль соискателя
                        </a>
                    </#if>
                </#if>
            </div>
        </div>
    </div>
</main>

<footer class="footer">
    <div class="footer-content">
        <p class="copyright">© 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>