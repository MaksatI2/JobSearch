<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/auth/register.css" activeNav="login">
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="card auth-card border-0 mb-5">
                <div class="card-body p-4">
                    <h2 class="card-title mb-4 pb-2 border-bottom border-primary"><@spring.message code="login.title"/></h2>

                    <#if loginError??>
                        <div class="alert alert-danger mb-4">
                            ${loginError}
                        </div>
                    </#if>

                    <#if registrationSuccess??>
                        <div class="alert alert-success mb-4">
                            ${registrationSuccess}
                        </div>
                    </#if>

                    <form action="/login" method="post">
                        <#if _csrf??>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </#if>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" id="email" name="username" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label"><@spring.message code="login.password"/></label>
                            <input type="password" id="password" name="password" class="form-control">
                        </div>
                        <button type="submit" class="btn btn-primary w-100 py-2 mb-3"><@spring.message code="login.submit"/></button>
                    </form>

                    <div class="mt-4">
                        <div class="d-flex align-items-center justify-content-center mb-3">
                            <span class="border-top flex-grow-1"></span>
                            <span class="mx-3 text-muted"><@spring.message code="login.or"/></span>
                            <span class="border-top flex-grow-1"></span>
                        </div>
                        <a href="/oauth2/authorization/google" class="btn btn-outline-secondary w-100 d-flex align-items-center justify-content-center">
                            <img src="/static/images/contact/gmail-icon-logo-svgrepo-com.svg" alt="Google logo" width="20" height="20" class="me-2">
                            <@spring.message code="login.google"/>
                        </a>
                    </div>

                    <div class="text-center mt-4">
                        <p class="text-muted mb-2"><@spring.message code="login.no.account"/> <a href="/auth/register" class="text-decoration-none"><@spring.message code="login.register"/></a></p>
                        <p class="text-muted mb-0"><a href="/auth/forgot_password" class="text-decoration-none"><@spring.message code="login.reset.password"/></a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.page>