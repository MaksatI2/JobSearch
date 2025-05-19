<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/auth/register.css" activeNav="login">
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="card auth-card border-0 mb-5">
                <div class="card-body p-4">
                    <h2 class="card-title mb-4 pb-2 border-bottom border-primary"><@spring.message code="choose.account.title"/></h2>

                    <div class="bg-light rounded p-3 mb-4">
                        <p class="mb-1"><@spring.message code="choose.account.name"/> <strong>${name}</strong></p>
                        <p class="mb-0">Email: <strong>${email}</strong></p>
                    </div>

                    <form action="/auth/oauth2/account-type" method="post">
                        <#if _csrf??>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </#if>

                        <div class="mb-4">
                            <div class="card mb-3 border-0">
                                <div class="card-body p-3 rounded">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" id="applicant" name="accountType" value="1" checked>
                                        <label class="form-check-label w-100" for="applicant">
                                            <div class="ms-2">
                                                <div class="fw-bold fs-5"><@spring.message code="choose.account.applicant"/></div>
                                                <div class="text-muted"><@spring.message code="choose.account.applicant.desc"/></div>
                                            </div>
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="card border-0">
                                <div class="card-body p-3 rounded">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" id="employer" name="accountType" value="2">
                                        <label class="form-check-label w-100" for="employer">
                                            <div class="ms-2">
                                                <div class="fw-bold fs-5"><@spring.message code="choose.account.employer"/></div>
                                                <div class="text-muted"><@spring.message code="choose.account.employer.desc"/></div>
                                            </div>
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 mb-3"><@spring.message code="choose.account.go"/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@layout.page>