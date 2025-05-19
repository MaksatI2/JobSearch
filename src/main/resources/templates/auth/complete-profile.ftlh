<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>
<@layout.page styles="/static/css/auth/register.css" activeNav="">
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="card auth-card border-0 mb-5">
                <div class="card-body p-4">
                    <h2 class="card-title mb-4 pb-2 border-bottom border-primary"><@spring.message code="complete.title"/></h2>

                    <#if error??>
                        <div class="alert alert-danger mb-4">
                            ${error}
                        </div>
                    </#if>

                    <form action="/auth/oauth2/complete-profile" method="post">
                        <#if _csrf??>
                            <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                                   value="${(_csrf.token)!'csrf-token'}"/>
                        </#if>

                        <#if accountType.name() == "APPLICANT">
                            <div class="mb-3">
                                <label for="surname" class="form-label"><@spring.message code="register.applicant.surname"/></label>
                                <input type="text" id="surname" name="surname" class="form-control <#if errorField?? && errorField == 'surname'>is-invalid</#if>"
                                       value="${(surname)!''}"/>
                                <small class="form-text text-muted"><@spring.message code="complete.surname"/></small>
                            </div>
                            <div class="mb-3">
                                <label for="age" class="form-label"><@spring.message code="register.applicant.age"/></label>
                                <input type="number" id="age" name="age" class="form-control <#if errorField?? && errorField == 'age'>is-invalid</#if>"
                                       value="${(age)!''}"/>
                                <small class="form-text text-muted"><@spring.message code="complete.age"/></small>
                            </div>
                        </#if>

                        <div class="mb-4">
                            <label for="phoneNumber" class="form-label"><@spring.message code="register.phone.number"/></label>
                            <input type="tel" id="phoneNumber" name="phoneNumber" class="form-control <#if errorField?? && errorField == 'phoneNumber'>is-invalid</#if>"
                                   value="${(phoneNumber)!''}"/>
                            <small class="form-text text-muted"><@spring.message code="complete.phone"/></small>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 mb-3"><@spring.message code="complete.save"/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@layout.page>