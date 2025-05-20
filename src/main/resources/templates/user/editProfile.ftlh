<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/user/editProfile.css" activeNav="profile">
    <div class="container py-4">
        <h1 class="page-title mb-4"><@spring.message code="profile.edit.title"/></h1>

        <@spring.bind "editUserDTO" />
        <#if spring.status.error>
            <div class="global-error alert alert-danger">
                <#list spring.status.errorMessages as error>
                    <div>${error}</div>
                </#list>
            </div>
        </#if>

        <form action="/profile/edit" method="post" class="profile-form bg-white p-4 rounded shadow">
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                       value="${(_csrf.token)!'csrf-token'}"/>
            </#if>

            <div class="row mb-3">
                <div class="col-md-6 mb-3 mb-md-0">
                    <div class="form-group">
                        <label for="name" class="form-label"><@spring.message code="profile.edit.name"/></label>
                        <@spring.formInput "editUserDTO.name", "class='form-control' + ((spring.status.errors['name']?has_content)?then(' is-invalid', ''))", "id='name'" />
                        <@spring.showErrors "<br>" "invalid-feedback" />
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label for="surname" class="form-label"><@spring.message code="profile.edit.surname"/></label>
                        <@spring.formInput "editUserDTO.surname", "class='form-control' + ((spring.status.errors['surname']?has_content)?then(' is-invalid', ''))", "id='surname'" />
                        <@spring.showErrors "<br>" "invalid-feedback" />
                    </div>
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-md-6 mb-3 mb-md-0">
                    <div class="form-group">
                        <label for="age" class="form-label"><@spring.message code="profile.edit.age"/></label>
                        <@spring.formInput "editUserDTO.age", "class='form-control' + ((spring.status.errors['age']?has_content)?then(' is-invalid', ''))", "id='age'" />
                        <@spring.showErrors "<br>" "invalid-feedback" />
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label for="phoneNumber" class="form-label"><@spring.message code="profile.edit.phone"/></label>
                        <@spring.formInput "editUserDTO.phoneNumber", "class='form-control' + ((spring.status.errors['phoneNumber']?has_content)?then(' is-invalid', ''))", "id='phoneNumber'" />
                        <@spring.showErrors "<br>" "invalid-feedback" />
                    </div>
                </div>
            </div>

            <div class="button-group d-flex gap-3 mt-4">
                <button type="submit" class="submit-btn btn btn-success flex-grow-1"><@spring.message code="profile.edit.save"/></button>
                <a href="/profile" class="btn btn-secondary flex-grow-1"><@spring.message code="profile.edit.back"/></a>
            </div>
        </form>
    </div>
</@layout.page>