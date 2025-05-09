<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/auth/register.css" activeNav="login">
    <div class="registration-form-container">
        <h1 class="page-title"><@spring.message code="choose.account.title"/></h1>

        <div class="user-info">
            <p><@spring.message code="choose.account.name"/> ${name}</p>
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
                        <span class="account-type-title"><@spring.message code="choose.account.applicant"/></span>
                        <span class="account-type-description"><@spring.message code="choose.account.applicant.desc"/></span>
                    </label>
                </div>

                <div class="account-type-option">
                    <input type="radio" id="employer" name="accountType" value="2">
                    <label for="employer" class="account-type-label">
                        <span class="account-type-title"><@spring.message code="choose.account.employer"/></span>
                        <span class="account-type-description"><@spring.message code="choose.account.employer.desc"/></span>
                    </label>
                </div>
            </div>

            <button type="submit" class="submit-btn"><@spring.message code="choose.account.go"/></button>
        </form>
    </div>
</@layout.page>