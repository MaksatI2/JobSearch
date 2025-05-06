<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page title="Регистрация | JobSearch" styles="/static/css/auth/register.css" activeNav="register">
    <div class="registration-form-container">
        <h1 class="page-title">Регистрация</h1>

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
                    <#list spring.status.errorMessages as error>
                        <div>${error}</div>
                    </#list>
                </div>
            </#if>

            <form action="/auth/register/applicant" method="post" class="registration-form">
                <#if _csrf??>
                    <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                           value="${(_csrf.token)!'csrf-token'}"/>
                </#if>
                <div class="form-row">
                    <div class="form-group">
                        <label for="name">Имя</label>
                        <@spring.formInput "applicantRegisterDTO.name", "class='form-control' + ((spring.status.error && spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
                        <@spring.showErrors "<br>" "error" />
                    </div>
                    <div class="form-group">
                        <label for="surname">Фамилия</label>
                        <@spring.formInput "applicantRegisterDTO.surname", "class='form-control' + ((spring.status.error && spring.status.errors['surname']?has_content)?then(' error-field', ''))", "id='surname'" />
                        <@spring.showErrors "<br>" "error" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="age">Возраст</label>
                    <@spring.formInput "applicantRegisterDTO.age", "class='form-control' + ((spring.status.error && spring.status.errors['age']?has_content)?then(' error-field', ''))", "id='age'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <@spring.formInput "applicantRegisterDTO.email", "class='form-control' + ((spring.status.error && spring.status.errors['email']?has_content)?then(' error-field', ''))", "id='email'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="password">Пароль (минимум 6 символов)</label>
                    <@spring.formPasswordInput "applicantRegisterDTO.password"
                    "class='form-control' + ((spring.status.error && spring.status.errors['password']?has_content)?then(' error-field', ''))" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="phoneNumber">Номер телефона</label>
                    <@spring.formInput "applicantRegisterDTO.phoneNumber", "class='form-control' + ((spring.status.error && spring.status.errors['phoneNumber']?has_content)?then(' error-field', ''))", "id='phoneNumber'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <button type="submit" class="submit-btn">Зарегистрироваться</button>
            </form>
        <#else>
            <@spring.bind "employerRegisterDTO" />
            <#if spring.status.error>
                <div class="global-error">
                    <#list spring.status.errorMessages as error>
                        <div>${error}</div>
                    </#list>
                </div>
            </#if>

            <form action="/auth/register/employer" method="post" class="registration-form">
                <#if _csrf??>
                    <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                           value="${(_csrf.token)!'csrf-token'}"/>
                </#if>
                <div class="form-group">
                    <label for="companyName">Название компании</label>
                    <@spring.formInput "employerRegisterDTO.companyName", "class='form-control' + ((spring.status.error && spring.status.errors['companyName']?has_content)?then(' error-field', ''))", "id='companyName'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <@spring.formInput "employerRegisterDTO.email", "class='form-control' + ((spring.status.error && spring.status.errors['email']?has_content)?then(' error-field', ''))", "id='email'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="password">Пароль (минимум 6 символов)</label>
                    <@spring.formPasswordInput "employerRegisterDTO.password"
                    "class='form-control' + ((spring.status.error && spring.status.errors['password']?has_content)?then(' error-field', ''))" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="phoneNumber">Номер телефона</label>
                    <@spring.formInput "employerRegisterDTO.phoneNumber", "class='form-control' + ((spring.status.error && spring.status.errors['phoneNumber']?has_content)?then(' error-field', ''))", "id='phoneNumber'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <button type="submit" class="submit-btn">Зарегистрироваться</button>
            </form>
        </#if>

        <div class="login-link">
            Уже есть аккаунт? <a href="/auth/login" class="text-link">Войти</a>
        </div>
    </div>
</@layout.page>