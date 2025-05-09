<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/user/editProfile.css" activeNav="profile">
    <h1 class="page-title"><@spring.message code="profile.edit.title"/></h1>

    <@spring.bind "editUserDTO" />
    <#if spring.status.error>
        <div class="global-error">
            <#list spring.status.errorMessages as error>
                <div>${error}</div>
            </#list>
        </div>
    </#if>

    <form action="/profile/edit" method="post" class="profile-form">
        <#if _csrf??>
            <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                   value="${(_csrf.token)!'csrf-token'}"/>
        </#if>

        <div class="form-row">
            <div class="form-group">
                <label for="name"><@spring.message code="profile.edit.name"/></label>
                <@spring.formInput "editUserDTO.name", "class='form-control' + ((spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="surname"><@spring.message code="profile.edit.surname"/></label>
                <@spring.formInput "editUserDTO.surname", "class='form-control' + ((spring.status.errors['surname']?has_content)?then(' error-field', ''))", "id='surname'" />
                <@spring.showErrors "<br>" "error" />
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="age"><@spring.message code="profile.edit.age"/></label>
                <@spring.formInput "editUserDTO.age", "class='form-control' + ((spring.status.errors['age']?has_content)?then(' error-field', ''))", "id='age'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="phoneNumber"><@spring.message code="profile.edit.phone"/></label>
                <@spring.formInput "editUserDTO.phoneNumber", "class='form-control' + ((spring.status.errors['phoneNumber']?has_content)?then(' error-field', ''))", "id='phoneNumber'" />
                <@spring.showErrors "<br>" "error" />
            </div>
        </div>

        <div class="button-group">
            <button type="submit" class="submit-btn"><@spring.message code="profile.edit.save"/></button>
            <a href="/profile" class="btn btn-secondary"><@spring.message code="profile.edit.back"/></a>
        </div>
    </form>
</@layout.page>
