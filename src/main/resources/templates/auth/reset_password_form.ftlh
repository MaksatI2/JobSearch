<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/auth/register.css" activeNav="">
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="card auth-card border-0 mb-5">
                <div class="card-body p-4">
                    <h2 class="card-title mb-4 pb-2 border-bottom border-primary">
                        <@spring.message code="password.reset.title"/>
                    </h2>

                    <form action="/auth/reset_password" method="post">
                        <#if _csrf??>
                            <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                                   value="${(_csrf.token)!'csrf-token'}"/>
                        </#if>

                        <@spring.formHiddenInput "resetRequest.token"/>

                        <div class="mb-4">
                            <label for="password" class="form-label">
                                <@spring.message code="password.reset.new"/>
                            </label>
                            <@spring.formPasswordInput "resetRequest.password",
                            "class='form-control' + ((spring.status.error)?then(' is-invalid', ''))"/>
                            <@spring.showErrors "<br>", "invalid-feedback"/>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 mb-3">
                            <@spring.message code="password.reset.save"/>
                        </button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="/auth/login" class="text-decoration-none">
                            <@spring.message code="password.reset.back.login"/>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.page>