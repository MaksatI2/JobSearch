<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page title="Сброс пароля | JobSearch" styles="/static/css/auth/register.css" activeNav="">
    <div class="registration-form-container">
        <h1 class="page-title">Сброс пароля</h1>

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

        <form action="/auth/reset_password" method="post" class="registration-form">
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                       value="${(_csrf.token)!'csrf-token'}"/>
            </#if>
            <input type="hidden" name="token" value="${token!''}"/>

            <div class="form-group">
                <label for="password">Новый пароль</label>
                <@spring.formInput "resetRequest.password", "class='form-control' placeholder='Введите новый пароль' required", "password"/>
                <@spring.showErrors "<br>", "field-error"/>
            </div>

            <button type="submit" class="submit-btn">Изменить пароль</button>
        </form>

        <div class="login-link">
            <a href="/auth/login">Вернуться к входу</a>
        </div>
    </div>
</@layout.page>