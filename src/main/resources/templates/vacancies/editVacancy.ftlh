<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page
styles="/static/css/vacancies/vacancies.css"
activeNav="vacancies">

    <h1 class="page-title"><@spring.message code="vacancies.editing.title"/></h1>

    <@spring.bind "vacancyForm" />
    <#if spring.status.error>
        <div class="global-error">
            <#list spring.status.errorMessages as error>
                <div>${error}</div>
            </#list>
        </div>
    </#if>

    <form action="/vacancies/${vacancyId}/edit" method="post" class="vacancy-form">
        <#if _csrf??>
            <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                   value="${(_csrf.token)!'csrf-token'}"/>
        </#if>
        <div class="form-group">
            <label for="name"><@spring.message code="vacancies.editing.vacancy.name"/></label>
            <@spring.formInput "vacancyForm.name", "class='form-control' + ((spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
            <@spring.showErrors "<br>" "error" />
        </div>

        <div class="form-group">
            <label for="categoryId"><@spring.message code="vacancies.editing.vacancy.category"/></label>
            <select class="form-control" id="categoryId" name="categoryId">
                <#list categories as category>
                    <option value="${category.id}" <#if vacancyForm.categoryId == category.id>selected</#if>>${category.name}</option>
                </#list>
            </select>
            <@spring.showErrors "<br>" "error" />
        </div>

        <div class="form-group">
            <label for="description"><@spring.message code="vacancies.editing.description"/></label>
            <@spring.formInput "vacancyForm.description", "class='form-control' + ((spring.status.errors['description']?has_content)?then(' error-field', ''))", "id='description'" />
            <@spring.showErrors "<br>" "error" />
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="salary"><@spring.message code="vacancies.editing.salary"/></label>
                <@spring.formInput
                "vacancyForm.salary",
                "type='number' min='0' class='form-control' + ((spring.status.errors['salary']?has_content)?then(' error-field', ''))",
                "id='salary'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="expFrom"><@spring.message code="vacancies.editing.exp.from"/></label>
                <@spring.formInput
                "vacancyForm.expFrom",
                "type='number' min='0' class='form-control' + ((spring.status.errors['expFrom']?has_content)?then(' error-field', ''))",
                "id='expFrom'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="expTo"><@spring.message code="vacancies.editing.exp.to"/></label>
                <@spring.formInput
                "vacancyForm.expTo",
                "type='number' min='0' class='form-control' + ((spring.status.errors['expTo']?has_content)?then(' error-field', ''))",
                "id='expTo'" />
                <@spring.showErrors "<br>" "error" />
            </div>
        </div>

        <div class="form-group">
            <label for="form-control"><@spring.message code="vacancies.editing.status"/></label>
            <select class="form-control" id="isActive" name="isActive">
                <option value="true" <#if vacancyForm.isActive?? && vacancyForm.isActive == true>selected</#if>><@spring.message code="vacancies.status.active"/></option>
                <option value="false" <#if vacancyForm.isActive?? && vacancyForm.isActive == false>selected</#if>><@spring.message code="vacancies.status.inactive"/></option>
            </select>
            <@spring.showErrors "isActive" "error" />
        </div>

        <button type="submit" class="submit-btn"><@spring.message code="vacancies.editing.save"/></button>
    </form>
</@layout.page>