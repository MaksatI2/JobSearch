<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Активные резюме | JobSearch</title>
    <link rel="stylesheet" href="/static/css/resumes/resumes.css">
    <link rel="stylesheet" href="/static/css/header.css">
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
        <h1 class="page-title">Список активных резюме</h1>

        <div class="filters-container">
            <form method="get" action="/resumes/allResumes" class="filter-form">
                <input type="hidden" name="page" value="0">
                <input type="hidden" name="size" value="${pageSize}">

                <div class="filter-group">
                    <label for="sort">Сортировка:</label>
                    <select id="sort" name="sort" class="form-control">
                        <option value="">По дате (новые)</option>
                        <option value="salaryAsc" <#if selectedSort?? && selectedSort == "salaryAsc">selected</#if>>
                            По зарплате (↑)
                        </option>
                        <option value="salaryDesc" <#if selectedSort?? && selectedSort == "salaryDesc">selected</#if>>
                            По зарплате (↓)
                        </option>
                    </select>
                </div>

                <button type="submit" class="submit-btn">Применить</button>
                <#if selectedSort??>
                    <a href="/vacancies?page=0&size=${pageSize}" class="reset-btn">Сбросить</a>
                </#if>
            </form>
        </div>

        <#if resumes?? && resumes?has_content>
            <div class="resumes-grid">
                <#list resumes as resume>
                    <a href="/resumes/${resume.id}/info" class="resume-link">
                        <article class="resume-card">
                            <div class="resume-header">
                                <h2 class="resume-title">${resume.name}</h2>
                                <span class="salary">${resume.salary} с</span>
                            </div>

                            <div class="resume-author">
                                <#if resume.applicantAvatar?? && resume.applicantAvatar?has_content>
                                    <img src="${resume.applicantAvatar}" alt="Аватар" class="avatar-small">
                                <#else>
                                    <svg class="icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                                    </svg>
                                </#if>
                                <span>${resume.applicantName!'Неизвестно'}</span>
                                <#if resume.applicantAge??>
                                    <span class="age">${resume.applicantAge} лет</span>
                                </#if>
                            </div>

                            <div class="resume-meta">
                                <span class="update-time">
                                    <svg class="icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                    </svg>
                                    ${resume.updateTime?string("dd.MM.yyyy")}
                                </span>
                            </div>
                            <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('APPLICANT')>
                                <#if resume.applicantId??>
                                    <a href="/profile/viewApplicant/${resume.applicantId}" class="applicant-profile-btn">
                                        Профиль соискателя
                                    </a>
                                </#if>
                            </#if>
                        </article>
                    </a>
                </#list>
            </div>

            <#if totalPages gt 1>
                <div class="pagination">
                    <#if currentPage gt 0>
                        <a href="/resumes/allResumes?page=0&size=${pageSize}" class="page-link">««</a>
                        <a href="/resumes/allResumes?page=${currentPage - 1}&size=${pageSize}" class="page-link">«</a>
                    </#if>

                    <#list 0..totalPages-1 as i>
                        <#if i == currentPage>
                            <span class="page-link active">${i + 1}</span>
                        <#else>
                            <a href="/resumes/allResumes?page=${i}&size=${pageSize}" class="page-link">${i + 1}</a>
                        </#if>
                    </#list>

                    <#if currentPage lt totalPages - 1>
                        <a href="/resumes/allResumes?page=${currentPage + 1}&size=${pageSize}" class="page-link">»</a>
                        <a href="/resumes/allResumes?page=${totalPages - 1}&size=${pageSize}" class="page-link">»»</a>
                    </#if>

                    <div class="page-size">
                        Элементов на странице:
                        <select onchange="window.location.href='/resumes/allResumes?page=0&size='+this.value" class="page-size-select">
                            <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                            <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                            <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                            <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                        </select>
                    </div>
                </div>
            </#if>
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
        <p>© 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>