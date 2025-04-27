<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page title="Восстановление пароля | JobSearch" styles="/static/css/auth/register.css" activeNav="">
    <div class="registration-form-container">
        <h1 class="page-title">Восстановление пароля</h1>

        <#if error??>
            <div class="global-error">
                ${error}
            </div>
        </#if>

        <#if message??>
            <div class="success-message">
                ${message}
            </div>
        </#if>

        <form action="/auth/forgot_password" method="post" class="registration-form">
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                       value="${(_csrf.token)!'csrf-token'}"/>
            </#if>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="Введите ваш email" required autofocus>
            </div>

            <button type="submit" class="submit-btn">Отправить</button>
        </form>

        <div class="login-link">
            <a href="/auth/login">Вернуться к входу</a>
        </div>
    </div>
</@layout.page>