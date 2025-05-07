<#import "../layout.ftlh" as layout>
<@layout.page styles="/static/css/auth/register.css" activeNav="">
    <div class="registration-form-container">
        <h1>Завершение регистрации</h1>

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
                    <label for="surname">Фамилия</label>
                    <input type="text" id="surname" name="surname" class="form-control <#if errorField?? && errorField == 'surname'>is-invalid</#if>"
                           value="${(surname)!''}" title="Только буквы и дефисы"/>
                    <small class="form-text text-muted">Только буквы и дефисы</small>
                </div>
                <div class="form-group">
                    <label for="age">Возраст</label>
                    <input type="number" id="age" name="age" class="form-control <#if errorField?? && errorField == 'age'>is-invalid</#if>"
                           value="${(age)!''}" title="От 18 до 60 лет"/>
                    <small class="form-text text-muted">От 18 до 60 лет</small>
                </div>
            </#if>

            <div class="form-group">
                <label for="phoneNumber">Номер телефона</label>
                <input type="tel" id="phoneNumber" name="phoneNumber" class="form-control <#if errorField?? && errorField == 'phoneNumber'>is-invalid</#if>"
                       value="${(phoneNumber)!''}" title="Формат: +996700123456"/>
                <small class="form-text text-muted">Формат: +996700123456</small>
            </div>

            <button type="submit" class="submit-btn">Завершить регистрацию</button>
        </form>
    </div>
</@layout.page>