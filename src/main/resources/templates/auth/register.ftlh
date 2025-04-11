<#-- register.ftlh -->
<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link rel="stylesheet" href="/static/css/register.css">
</head>
<body>
<div class="form-container">
    <h2 style="text-align: center; margin-bottom: 25px;">Регистрация</h2>

    <#if springMacroRequestContext.getRequestUri()?contains("error")>
        <div class="global-error">
            Произошла ошибка при регистрации. Пожалуйста, проверьте введенные данные.
        </div>
    </#if>

    <div class="account-type-selector">
        <a href="/auth/register?type=applicant" class="account-type-btn <#if (type!'applicant') == 'applicant'>active</#if>">Я соискатель</a>
        <a href="/auth/register?type=employer" class="account-type-btn <#if (type!'applicant') == 'employer'>active</#if>">Я работодатель</a>
    </div>

    <#if (type!'applicant') == 'applicant'>
        <@spring.bind "applicantRegisterDTO" />
        <#if spring.status.error>
            <div class="global-error">
                Пожалуйста, исправьте ошибки в форме
            </div>
        </#if>

        <form action="/auth/register/applicant" method="post">
            <div class="form-group">
                <label for="name">Имя:</label>
                <@spring.formInput "applicantRegisterDTO.name", "class='form-control' + (spring.status.error?then(' error-field', ''))" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="surname">Фамилия:</label>
                <@spring.formInput "applicantRegisterDTO.surname", "class='form-control' + (spring.status.error?then(' error-field', ''))" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="age">Возраст:</label>
                <@spring.formInput "applicantRegisterDTO.age", "class='form-control' + (spring.status.error?then(' error-field', ''))" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <@spring.formInput "applicantRegisterDTO.email", "class='form-control' + (spring.status.error?then(' error-field', ''))" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="password">Пароль (минимум 6 символов):</label>
                <@spring.formPasswordInput "applicantRegisterDTO.password", "class='form-control' + (spring.status.error?then(' error-field', ''))" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="phoneNumber">Номер телефона:</label>
                <@spring.formInput "applicantRegisterDTO.phoneNumber", "class='form-control' + (spring.status.error?then(' error-field', ''))" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <button type="submit" class="submit-btn">Зарегистрироваться</button>
        </form>
    <#else>
        <@spring.bind "employerRegisterDTO" />
        <#if spring.status.error>
            <div class="global-error">
                Пожалуйста, исправьте ошибки в форме
            </div>
        </#if>

        <form action="/auth/register/employer" method="post">
            <div class="form-group">
                <label for="companyName">Название компании:</label>
                <@spring.formInput "employerRegisterDTO.companyName", "class='form-control' + (spring.status.error?then(' error-field', ''))" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <@spring.formInput "employerRegisterDTO.email", "class='form-control' + (spring.status.error?then(' error-field', ''))" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="password">Пароль (минимум 6 символов):</label>
                <@spring.formPasswordInput "employerRegisterDTO.password", "class='form-control' + (spring.status.error?then(' error-field', ''))" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="phoneNumber">Номер телефона:</label>
                <@spring.formInput "employerRegisterDTO.phoneNumber", "class='form-control' + (spring.status.error?then(' error-field', ''))" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <button type="submit" class="submit-btn">Зарегистрироваться</button>
        </form>
    </#if>

    <div class="login-link">
        Уже есть аккаунт? <a href="/auth/login">Войти</a>
    </div>
</div>
</body>
</html>