<#import "/spring.ftl" as spring>
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

<#--            пока оставил так проверку на фронте думаю когда будет жс поменять-->
<#--            пока не могу додуматься как сделать валидацию на бэке-->
            <div class="form-group">
                <label for="categoryId">Категория:</label>
                <select id="categoryId" name="categoryId" required>
                    <option value="">Выберите категорию</option>
                    <#list categories as category>
                        <option value="${category.id}">${category.name}</option>
                    </#list>
                </select>
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
                    "type='number' min='0' class='form-control' + ((spring.status.error && spring.status.errors['salary']?has_content)?then(' error-field', '')) required",
                    "id='salary'" />
                    <@spring.showErrors "<br>" "error" />
                </div>

                <div class="form-group">
                    <label for="expFrom">Опыт от</label>
                    <@spring.formInput
                    "vacancyForm.expFrom",
                    "type='number' min='0' class='form-control' + ((spring.status.error && spring.status.errors['expFrom']?has_content)?then(' error-field', '')) required",
                    "id='expFrom'" />
                    <@spring.showErrors "<br>" "error" />
                </div>

                <div class="form-group">
                    <label for="expTo">Опыт до</label>
                    <@spring.formInput
                    "vacancyForm.expTo",
                    "type='number' min='0' class='form-control' + ((spring.status.error && spring.status.errors['expTo']?has_content)?then(' error-field', '')) required",
                    "id='expTo'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
            </div>

            <#--            пока оставил так проверку на фронте думаю когда будет жс поменять-->
            <#--            пока не могу додуматься как сделать валидацию на бэке
                            можно но муторно -->
                <div class="form-group">
                    <label for="isActive">Статус вакансии</label>
                    <select id="isActive" name="isActive" required>
                        <option value="">Выберите статус</option>
                        <option value="true" <#if vacancyForm.isActive?? && vacancyForm.isActive == true>selected</#if>>Активна</option>
                        <option value="false" <#if vacancyForm.isActive?? && vacancyForm.isActive == false>selected</#if>>Неактивна</option>
                    </select>
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