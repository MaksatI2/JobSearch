<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создать вакансию | JobSearch</title>
    <link rel="stylesheet" href="/static/css/vacancies.css">
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
        <h1 class="page-title">Создать вакансию</h1>

        <#if error??>
            <div class="alert alert-danger">${error}</div>
        </#if>

        <form action="/vacancies/create" method="post" class="vacancy-form">
            <div class="form-group">
                <label for="name">Название вакансии</label>
                <input type="text" id="name" name="name" required
                       minlength="2" maxlength="100"
                       value="${vacancyForm.name!''}">
                <#if nameError??>
                    <span class="error">${nameError}</span>
                </#if>
            </div>

            <div class="form-group">
                <label for="categoryId">Категория:</label>
                <select id="categoryId" name="categoryId">
                    <option value="">Выберите категорию</option>
                    <#list categories as category>
                        <option value="${category.id}">${category.name}</option>
                    </#list>
                </select>
            </div>


            <div class="form-group">
                <label for="description">Описание</label>
                <textarea id="description" name="description" required
                          minlength="10" maxlength="1000">${vacancyForm.description!''}</textarea>
                <#if descriptionError??>
                    <span class="error">${descriptionError}</span>
                </#if>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="salary">Зарплата</label>
                    <input type="number" id="salary" name="salary" required
                           min="0" step="0.01"
                           value="${vacancyForm.salary!''}">
                    <#if salaryError??>
                        <span class="error">${salaryError}</span>
                    </#if>
                </div>

                <div class="form-group">
                    <label for="expFrom">Опыт работы от (лет)</label>
                    <input type="number" id="expFrom" name="expFrom" required
                           min="0" max="50"
                           value="${vacancyForm.expFrom!''}">
                    <#if expFromError??>
                        <span class="error">${expFromError}</span>
                    </#if>
                </div>

                <div class="form-group">
                    <label for="expTo">Опыт работы до (лет)</label>
                    <input type="number" id="expTo" name="expTo" required
                           min="0" max="50"
                           value="${vacancyForm.expTo!''}">
                    <#if expToError??>
                        <span class="error">${expToError}</span>
                    </#if>
                </div>

                <div class="form-group">
                    <label for="isActive">Статус вакансии</label>
                    <select id="isActive" name="isActive" required>
                        <option value="">Выберите статус</option>
                        <option value="true" <#if vacancyForm.isActive?? && vacancyForm.isActive == true>selected</#if>>Активна</option>
                        <option value="false" <#if vacancyForm.isActive?? && vacancyForm.isActive == false>selected</#if>>Неактивна</option>
                    </select>
                </div>

            </div>

            <button type="submit" class="submit-btn">Создать вакансию</button>
        </form>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>© 2023 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>