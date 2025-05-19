<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/auth/register.css" activeNav="register">
    <script src="/static/Js/auth/register.js"></script>
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="card auth-card border-0 mb-5">
                <div class="card-body p-5">
                    <h2 class="card-title mb-4 pb-3 border-bottom border-primary text-center">
                        <@spring.message code="register.title"/>
                    </h2>

                    <#if springMacroRequestContext.getRequestUri()?contains("error")>
                        <div class="alert alert-danger mb-4 text-center rounded-2">
                            <@spring.message code="register.error"/>
                        </div>
                    </#if>

                    <div class="d-flex justify-content-center mb-4">
                        <div class="nav nav-pills bg-white bg-opacity-75 shadow rounded-pill px-2 py-1 gap-2">
                            <button type="button"
                                    class="nav-link px-4 py-2 rounded-pill fw-semibold active"
                                    data-type="applicant"
                                    onclick="showForm('applicant')">
                                <@spring.message code="register.applicant"/>
                            </button>
                            <button type="button"
                                    class="nav-link px-4 py-2 rounded-pill fw-semibold"
                                    data-type="employer"
                                    onclick="showForm('employer')">
                                <@spring.message code="register.employer"/>
                            </button>
                        </div>
                    </div>

                    <div id="form-applicant-block">
                        <@spring.bind "applicantRegisterDTO" />
                        <form action="/auth/register/applicant" method="post" class="needs-validation" id="form-applicant">
                            <#if _csrf??>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </#if>
                            <div class="row mb-3">
                                <div class="col-md-6 mb-3 mb-md-0">
                                    <label for="name" class="form-label"><@spring.message code="register.applicant.name"/></label>
                                    <@spring.formInput "applicantRegisterDTO.name", "class='form-control rounded-2'", "id='name'" />
                                    <@spring.showErrors "<br>" "invalid-feedback" />
                                </div>
                                <div class="col-md-6">
                                    <label for="surname" class="form-label"><@spring.message code="register.applicant.surname"/></label>
                                    <@spring.formInput "applicantRegisterDTO.surname", "class='form-control rounded-2'", "id='surname'" />
                                    <@spring.showErrors "<br>" "invalid-feedback" />
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="age" class="form-label"><@spring.message code="register.applicant.age"/></label>
                                <@spring.formInput "applicantRegisterDTO.age", "class='form-control rounded-2'", "id='age'" />
                                <@spring.showErrors "<br>" "invalid-feedback" />
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <@spring.formInput "applicantRegisterDTO.email", "class='form-control rounded-2'", "id='email'" />
                                <@spring.showErrors "<br>" "invalid-feedback" />
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label"><@spring.message code="register.password"/></label>
                                <@spring.formPasswordInput "applicantRegisterDTO.password", "class='form-control rounded-2'" />
                                <@spring.showErrors "<br>" "invalid-feedback" />
                            </div>
                            <div class="mb-4">
                                <label for="phoneNumber" class="form-label"><@spring.message code="register.phone.number"/></label>
                                <@spring.formInput "applicantRegisterDTO.phoneNumber", "class='form-control rounded-2'", "id='phoneNumber'" />
                                <@spring.showErrors "<br>" "invalid-feedback" />
                            </div>
                            <button type="submit" class="btn btn-primary w-100 py-2 rounded-2 fw-semibold">
                                <@spring.message code="register.submit"/>
                            </button>
                        </form>
                    </div>

                    <div id="form-employer-block" style="display: none;">
                        <@spring.bind "employerRegisterDTO" />
                        <form action="/auth/register/employer" method="post" class="needs-validation" id="form-employer">
                            <#if _csrf??>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </#if>
                            <div class="mb-3">
                                <label for="companyName" class="form-label"><@spring.message code="register.employer.name"/></label>
                                <@spring.formInput "employerRegisterDTO.companyName", "class='form-control rounded-2'", "id='companyName'" />
                                <@spring.showErrors "<br>" "invalid-feedback" />
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <@spring.formInput "employerRegisterDTO.email", "class='form-control rounded-2'", "id='email'" />
                                <@spring.showErrors "<br>" "invalid-feedback" />
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label"><@spring.message code="register.password"/></label>
                                <@spring.formPasswordInput "employerRegisterDTO.password", "class='form-control rounded-2'" />
                                <@spring.showErrors "<br>" "invalid-feedback" />
                            </div>
                            <div class="mb-4">
                                <label for="phoneNumber" class="form-label"><@spring.message code="register.phone.number"/></label>
                                <@spring.formInput "employerRegisterDTO.phoneNumber", "class='form-control rounded-2'", "id='phoneNumber'" />
                                <@spring.showErrors "<br>" "invalid-feedback" />
                            </div>
                            <button type="submit" class="btn btn-primary w-100 py-2 rounded-2 fw-semibold">
                                <@spring.message code="register.submit"/>
                            </button>
                        </form>
                    </div>

                    <div class="text-center mt-4">
                        <p class="text-muted mb-0">
                            <@spring.message code="register.have.account"/>
                            <a href="/auth/login" class="text-decoration-none fw-medium">
                                <@spring.message code="register.login"/>
                            </a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.page>