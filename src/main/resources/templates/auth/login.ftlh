<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page title="Вход | JobSearch" styles="/static/css/auth/register.css" activeNav="login">
    <div class="registration-form-container">
        <h1 class="page-title">Вход в систему</h1>

        <#if loginError??>
            <div class="global-error">
                ${loginError}
            </div>
        </#if>

        <#if registrationSuccess??>
            <div class="alert-success">
                ${registrationSuccess}
            </div>
        </#if>

        <form action="/login" method="post" class="registration-form">
            <#if _csrf??>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </#if>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="username" class="form-control">
            </div>
            <div class="form-group">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" class="form-control">
            </div>
            <button type="submit" class="submit-btn">Войти</button>
        </form>

        <div class="oauth-login">
            <div class="separator">или</div>
            <a href="/oauth2/authorization/google" class="google-login-btn">
                <img src="/static/images/contact/gmail-icon-logo-svgrepo-com.svg" alt="Google logo" class="google-logo">
                Войти через Google
            </a>
        </div>

        <div class="login-link">
            Ещё нет аккаунта? <a href="/auth/register">Зарегистрироваться</a>
        </div>
        <div class="login-link">
            <a href="/auth/forgot_password">Забыли пароль?</a>
        </div>
    </div>
</@layout.page>