<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создать вакансию | JobSearch</title>
    <link rel="stylesheet" href="/static/css/vacancies/createVacancy.css">
    <link rel="stylesheet" href="/static/css/header.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="icon" href="/statiс/images/logo-transparent.png" type="image/x-icon">
</head>
<body>
<header class="header">
    <nav class="navbar">
        <img src="/static/images/logo-transparent.png" alt="JobSearch" class="logo-img">
        <div class="nav-links">
            <a href="/" class="nav-link">Главная</a>
            <a href="/vacancies" class="nav-link active">Вакансии</a>
            <a href="/resumes/allResumes" class="nav-link">Резюме</a>
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
        <h1 class="page-title">Создать вакансию</h1>

        <@spring.bind "vacancyForm" />
        <#if spring.status.error>
            <div class="global-error">
                <#list spring.status.errorMessages as error>
                    <div>${error}</div>
                </#list>
            </div>
        </#if>

        <form action="/vacancies/create" method="post" class="vacancy-form">
            <div class="form-group">
                <label for="name">Название вакансии</label>
                <@spring.formInput "vacancyForm.name", "class='form-control' + ((spring.status.error && spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <@spring.bind "vacancyForm.categoryId"/>
                <select class="form-select ${spring.status.error?string('is-invalid', '')}"
                        id="categoryId" name="${spring.status.expression}" onchange="toggleFields()">
                    <option value="">Выберите категорию</option>
                    <#list categories as category>
                        <option value="${category.id}" ${(spring.stringStatusValue == category.id?string)?string("selected", "")}>${category.name}</option>
                    </#list>
                </select>
                <@spring.showErrors "<br>", "error" />
            </div>


            <div class="form-group">
                <label for="description">Описание</label>
                <@spring.formInput "vacancyForm.description", "class='form-control' + ((spring.status.error && spring.status.errors['description']?has_content)?then(' error-field', ''))", "id='description'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="salary">Зарплата</label>
                    <@spring.formInput
                    "vacancyForm.salary",
                    "type='number' min='0' class='form-control' + ((spring.status.error && spring.status.errors['salary']?has_content)?then(' error-field', ''))",
                    "id='salary'" />
                    <@spring.showErrors "<br>" "error" />
                </div>

                <div class="form-group">
                    <label for="expFrom">Опыт от</label>
                    <@spring.formInput
                    "vacancyForm.expFrom",
                    "type='number' min='0' class='form-control' + ((spring.status.error && spring.status.errors['expFrom']?has_content)?then(' error-field', ''))",
                    "id='expFrom'" />
                    <@spring.showErrors "<br>" "error" />
                </div>

                <div class="form-group">
                    <label for="expTo">Опыт до</label>
                    <@spring.formInput
                    "vacancyForm.expTo",
                    "type='number' min='0' class='form-control' + ((spring.status.error && spring.status.errors['expTo']?has_content)?then(' error-field', ''))",
                    "id='expTo'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
            </div>

            <div class="form-group">
                <label for="isActive">Статус вакансии</label>
                <@spring.formSingleSelect "vacancyForm.isActive", {"true": "Активно", "false": "Неактивно"}, "class='form-control' id='isActive' required='required'" />
                <@spring.showErrors "<br>", "error" />
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