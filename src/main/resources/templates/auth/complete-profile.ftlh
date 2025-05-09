<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>
<@layout.page styles="/static/css/auth/register.css" activeNav="">
    <div class="registration-form-container">
        <h1><@spring.message code="complete.title"/></h1>

        <#if error??>
            <div class="global-error">
                ${error}
            </div>
        </#if>

        <form action="/auth/oauth2/complete-profile" method="post">
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                       value="${(_csrf.token)!'csrf-token'}"/>
            </#if>

            <#if accountType.name() == "APPLICANT">
                <div class="form-group">
                    <label for="surname"><@spring.message code="register.applicant.surname"/></label>
                    <input type="text" id="surname" name="surname" class="form-control <#if errorField?? && errorField == 'surname'>is-invalid</#if>"
                           value="${(surname)!''}"/>
                    <small class="form-text text-muted"><@spring.message code="complete.surname"/></small>
                </div>
                <div class="form-group">
                    <label for="age"><@spring.message code="register.applicant.age"/></label>
                    <input type="number" id="age" name="age" class="form-control <#if errorField?? && errorField == 'age'>is-invalid</#if>"
                           value="${(age)!''}"/>
                    <small class="form-text text-muted"><@spring.message code="complete.age"/></small>
                </div>
            </#if>

            <div class="form-group">
                <label for="phoneNumber"><@spring.message code="register.phone.number"/></label>
                <input type="tel" id="phoneNumber" name="phoneNumber" class="form-control <#if errorField?? && errorField == 'phoneNumber'>is-invalid</#if>"
                       value="${(phoneNumber)!''}"/>
                <small class="form-text text-muted"><@spring.message code="complete.phone"/></small>
            </div>

            <button type="submit" class="submit-btn"><@spring.message code="complete.save"/></button>
        </form>
    </div>
</@layout.page>