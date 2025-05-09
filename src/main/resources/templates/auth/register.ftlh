<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/auth/register.css" activeNav="register">
    <div class="registration-form-container">
        <h1 class="page-title"><@spring.message code="register.title"/></h1>

        <#if springMacroRequestContext.getRequestUri()?contains("error")>
            <div class="global-error">
                <@spring.message code="register.error"/>
            </div>
        </#if>

        <div class="account-type-selector">
            <a href="/auth/register?type=applicant" class="account-type-btn <#if (type!'applicant') == 'applicant'>active</#if>">
                <@spring.message code="register.applicant"/>
            </a>
            <a href="/auth/register?type=employer" class="account-type-btn <#if (type!'applicant') == 'employer'>active</#if>">
                <@spring.message code="register.employer"/>
            </a>
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
                        <label for="name"><@spring.message code="register.applicant.name"/></label>
                        <@spring.formInput "applicantRegisterDTO.name", "class='form-control' + ((spring.status.error && spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
                        <@spring.showErrors "<br>" "error" />
                    </div>
                    <div class="form-group">
                        <label for="surname"><@spring.message code="register.applicant.surname"/></label>
                        <@spring.formInput "applicantRegisterDTO.surname", "class='form-control' + ((spring.status.error && spring.status.errors['surname']?has_content)?then(' error-field', ''))", "id='surname'" />
                        <@spring.showErrors "<br>" "error" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="age"><@spring.message code="register.applicant.age"/></label>
                    <@spring.formInput "applicantRegisterDTO.age", "class='form-control' + ((spring.status.error && spring.status.errors['age']?has_content)?then(' error-field', ''))", "id='age'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <@spring.formInput "applicantRegisterDTO.email", "class='form-control' + ((spring.status.error && spring.status.errors['email']?has_content)?then(' error-field', ''))", "id='email'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="password"><@spring.message code="register.password"/></label>
                    <@spring.formPasswordInput "applicantRegisterDTO.password"
                    "class='form-control' + ((spring.status.error && spring.status.errors['password']?has_content)?then(' error-field', ''))" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="phoneNumber"><@spring.message code="register.phone.number"/></label>
                    <@spring.formInput "applicantRegisterDTO.phoneNumber", "class='form-control' + ((spring.status.error && spring.status.errors['phoneNumber']?has_content)?then(' error-field', ''))", "id='phoneNumber'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <button type="submit" class="submit-btn"><@spring.message code="register.submit"/></button>
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
                    <label for="companyName"><@spring.message code="register.employer.name"/></label>
                    <@spring.formInput "employerRegisterDTO.companyName", "class='form-control' + ((spring.status.error && spring.status.errors['companyName']?has_content)?then(' error-field', ''))", "id='companyName'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <@spring.formInput "employerRegisterDTO.email", "class='form-control' + ((spring.status.error && spring.status.errors['email']?has_content)?then(' error-field', ''))", "id='email'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="password"><@spring.message code="register.password"/></label>
                    <@spring.formPasswordInput "employerRegisterDTO.password"
                    "class='form-control' + ((spring.status.error && spring.status.errors['password']?has_content)?then(' error-field', ''))" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <div class="form-group">
                    <label for="phoneNumber"><@spring.message code="register.phone.number"/></label>
                    <@spring.formInput "employerRegisterDTO.phoneNumber", "class='form-control' + ((spring.status.error && spring.status.errors['phoneNumber']?has_content)?then(' error-field', ''))", "id='phoneNumber'" />
                    <@spring.showErrors "<br>" "error" />
                </div>
                <button type="submit" class="submit-btn"><@spring.message code="register.submit"/></button>
            </form>
        </#if>

        <div class="login-link">
            <@spring.message code="register.have.account"/> <a href="/auth/login" class="text-link"><@spring.message code="register.login"/></a>
        </div>
    </div>
</@layout.page>