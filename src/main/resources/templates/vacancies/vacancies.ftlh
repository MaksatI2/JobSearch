<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Список вакансий | JobSearch</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/vacancies/vacancies.css">
    <link rel="stylesheet" href="/static/css/header.css">
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
            <a href="/vacancies" class="nav-link active">Вакансии</a>
            <#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                <a href="/resumes/allResumes" class="nav-link">Резюме</a>
            <#else>
                <a href="/company/allCompanies" class="nav-link">Компании</a>
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
        <h1 class="page-title">Все вакансии</h1>

        <div class="filters-container">
            <form method="get" action="/vacancies" class="filter-form">
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
                        <option value="expAsc" <#if selectedSort?? && selectedSort == "expAsc">selected</#if>>
                            По опыту (↑)
                        </option>
                        <option value="expDesc" <#if selectedSort?? && selectedSort == "expDesc">selected</#if>>
                            По опыту (↓)
                        </option>
                        <option value="responsesDesc" <#if selectedSort?? && selectedSort == "responsesDesc">selected</#if>>
                            По количеству откликов (↓)
                        </option>
                    </select>
                </div>

                <button type="submit" class="submit-btn">Применить</button>
                <#if selectedSort??>
                    <a href="/vacancies?page=0&size=${pageSize}" class="reset-btn">Сбросить</a>
                </#if>
            </form>
        </div>

        <#if vacancies?size == 0>
            <div class="empty-state">
                <h3>Вакансии не найдены</h3>
                <p>В данный момент нет активных вакансий. Попробуйте позже.</p>
            </div>
        </#if>

        <div class="vacancies-grid">
            <#list vacancies as vacancy>
                <div class="vacancy-card">
                    <a href="/vacancies/${vacancy.id}/info" class="vacancy-card-link">
                        <div class="vacancy-header">
                            <h2 class="vacancy-title">${vacancy.name}</h2>
                            <div class="salary">${vacancy.salary} с</div>
                        </div>
                        <p class="vacancy-description">${vacancy.description}</p>
                        <div class="vacancy-meta">
                            <span class="experience">
                                <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                                </svg>
                                Опыт: ${vacancy.expFrom}-${vacancy.expTo} лет
                            </span>
                            <span class="author">
                                <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                                </svg>
                                Автор: ${vacancy.authorName!""}
                            </span>
                            <span class="update-time">
                                <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                                </svg>
                                Обновлена: ${vacancy.updateTime?string("dd.MM.yyyy")}
                            </span>
                        </div>
                    </a>
                    <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                        <#if vacancy.authorId??>
                            <a href="/profile/viewEmployer/${vacancy.authorId}" class="company-profile-btn">
                                Профиль компании
                            </a>
                        </#if>
                    </#if>
                </div>
            </#list>
        </div>

        <#if totalPages gt 1>
            <div class="pagination">
                <#if currentPage gt 0>
                    <a href="/vacancies?page=0&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                       class="page-link">««</a>
                    <a href="/vacancies?page=${currentPage - 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                       class="page-link">«</a>
                </#if>

                <#list 0..totalPages-1 as i>
                    <#if i == currentPage>
                        <span class="page-link active">${i + 1}</span>
                    <#else>
                        <a href="/vacancies?page=${i}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                           class="page-link">${i + 1}</a>
                    </#if>
                </#list>

                <#if currentPage lt totalPages - 1>
                    <a href="/vacancies?page=${currentPage + 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                       class="page-link">»</a>
                    <a href="/vacancies?page=${totalPages - 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                       class="page-link">»»</a>
                </#if>

                <div class="page-size">
                    Элементов на странице:
                    <select onchange="window.location.href='/vacancies?page=0&size='+this.value+'<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>'"
                            class="page-size-select">
                        <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                        <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                        <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                        <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                    </select>
                </div>
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