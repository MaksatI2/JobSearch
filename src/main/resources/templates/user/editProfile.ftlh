<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование профиля | JobSearch</title>
    <link rel="stylesheet" href="/static/css/user/editProfile.css">
    <link rel="stylesheet" href="/static/css/header.css">
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
    </div>
</header>

<main class="main">
    <div class="container">
        <h1 class="page-title">Редактирование профиля</h1>

        <@spring.bind "editUserDTO" />
        <#if spring.status.error>
            <div class="global-error">
                <#list spring.status.errorMessages as error>
                    <div>${error}</div>
                </#list>
            </div>
        </#if>

        <form action="/profile/edit" method="post" class="profile-form">
            <div class="form-row">
                <div class="form-group">
                    <label for="name">Имя</label>
                    <@spring.formInput "editUserDTO.name", "class='form-control' + ((spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
                    <@spring.showErrors "<br>" "error" />
                </div>

                <div class="form-group">
                    <label for="surname">Фамилия</label>
                    <@spring.formInput "editUserDTO.surname", "class='form-control' + ((spring.status.errors['surname']?has_content)?then(' error-field', ''))", "id='surname'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="age">Возраст</label>
                    <@spring.formInput "editUserDTO.age", "class='form-control' + ((spring.status.errors['age']?has_content)?then(' error-field', ''))", "id='age'" />
                    <@spring.showErrors "<br>" "error" />
                </div>

                <div class="form-group">
                    <label for="phoneNumber">Номер телефона</label>
                    <@spring.formInput "editUserDTO.phoneNumber", "class='form-control' + ((spring.status.errors['phoneNumber']?has_content)?then(' error-field', ''))", "id='phoneNumber'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
            </div>

            <div class="button-group">
                <button type="submit" class="submit-btn">Сохранить изменения</button>
                <a href="/profile" class="btn btn-secondary">Отмена</a>
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