<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/auth/register.css" activeNav="login">
    <div class="registration-form-container">
        <h1 class="page-title">Выберите тип аккаунта</h1>

        <div class="user-info">
            <p>Вы входите как: ${name}</p>
            <p>Email: ${email}</p>
        </div>

        <form action="/auth/oauth2/account-type" method="post" class="registration-form">
            <#if _csrf??>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </#if>

            <div class="form-group account-type-selection">
                <div class="account-type-option">
                    <input type="radio" id="applicant" name="accountType" value="1" checked>
                    <label for="applicant" class="account-type-label">
                        <span class="account-type-title">Соискатель</span>
                        <span class="account-type-description">Ищете работу, создаете резюме и откликаетесь на вакансии</span>
                    </label>
                </div>

                <div class="account-type-option">
                    <input type="radio" id="employer" name="accountType" value="2">
                    <label for="employer" class="account-type-label">
                        <span class="account-type-title">Работодатель</span>
                        <span class="account-type-description">Размещаете вакансии и ищете сотрудников</span>
                    </label>
                </div>
            </div>

            <button type="submit" class="submit-btn">Продолжить</button>
        </form>
    </div>
</@layout.page>