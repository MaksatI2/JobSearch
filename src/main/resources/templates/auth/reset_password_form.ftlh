<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/auth/register.css" activeNav="">
    <div class="registration-form-container">
        <h1 class="page-title"><@spring.message code="password.reset.title"/></h1>

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

            <div class="form-group">
                <label for="password"><@spring.message code="password.reset.new"/></label>
                <@spring.formHiddenInput "resetRequest.token"/>
                <@spring.formInput "resetRequest.password", "class='form-control'", "password"/>
                <@spring.showErrors "<br>", "error"/>
            </div>

            <button type="submit" class="submit-btn"><@spring.message code="password.reset.save"/></button>
        </form>

        <div class="login-link">
            <a href="/auth/login"><@spring.message code="password.reset.back.login"/></a>
        </div>
    </div>
</@layout.page>