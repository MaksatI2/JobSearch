<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование резюме | JobSearch</title>
    <link rel="stylesheet" href="/static/css/editResume.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
<header class="header">
    <div class="container">
        <nav class="navbar">
            <a href="/" class="logo">JobSearch</a>
            <div class="nav-links">
                <a href="/" class="nav-link">Главная</a>
                <a href="/vacancies" class="nav-link">Вакансии</a>
                <a href="/resumes/allResumes" class="nav-link">Резюме</a>
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
        <h1 class="page-title">Редактирование резюме</h1>

        <@spring.bind "resumeForm" />
        <#if spring.status.error>
            <div class="global-error">
                <#list spring.status.errorMessages as error>
                    <div>${error}</div>
                </#list>
            </div>
        </#if>

        <form action="/resumes/${resumeId}/edit" method="post" class="resume-form">
            <div class="section">
                <h2 class="section-title">Основная информация</h2>

                <div class="form-group">
                    <label for="name">Название резюме*</label>
                    <@spring.formInput "resumeForm.name", "class='form-control' id='name'" />
                    <@spring.showErrors "<br>", "error" />
                </div>

                <div class="form-group">
                    <label for="categoryId">Категория*</label>
                    <select class="form-control" id="categoryId" name="categoryId" required>
                        <option value="">Выберите категорию</option>
                        <#list categories as category>
                            <option value="${category.id}"
                                    <#if resumeForm.categoryId?? && resumeForm.categoryId == category.id>selected</#if>>
                                ${category.name}
                            </option>
                        </#list>
                    </select>
                    <@spring.showErrors "<br>", "error" />
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="salary">Желаемая зарплата</label>
                        <@spring.formInput "resumeForm.salary", "type='number' min='0' step='0.01' class='form-control' id='salary'" />
                        <@spring.showErrors "<br>", "error" />
                    </div>
                    <div class="form-group">
                        <label for="isActive">Статус резюме</label>
                        <@spring.formSingleSelect "resumeForm.isActive", {"true": "Активно", "false": "Неактивно"}, "class='form-control' id='isActive' required='required'" />
                        <@spring.showErrors "<br>", "error" />
                    </div>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="submit-btn">Сохранить изменения</button>
                <a href="/profile" class="cancel-btn">Отмена</a>
            </div>
        </form>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>© 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>