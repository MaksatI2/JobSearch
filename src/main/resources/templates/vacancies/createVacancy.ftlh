<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page title="Создать вакансию | JobSearch" styles="/static/css/vacancies/vacancies.css" activeNav="vacancies">
    <h1 class="page-title">Создать вакансию</h1>

    <@spring.bind "vacancyForm" />
    <#if spring.status.error>
        <div class="global-error">
            <#list spring.status.errorMessages as error>
                <div>${error}</div>
            </#list>
        </div>
    </#if>

    <form action="/vacancies/create" method="post" class="vacancy-form">
        <#if _csrf??>
            <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                   value="${(_csrf.token)!'csrf-token'}"/>
        </#if>
        <div class="form-group">
            <label for="name">Название вакансии</label>
            <@spring.formInput "vacancyForm.name", "class='form-control' + ((spring.status.error && spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
            <@spring.showErrors "<br>" "error" />
        </div>

        <div class="form-group">
            <@spring.bind "vacancyForm.categoryId"/>
            <select class="form-select ${spring.status.error?string('is-invalid', '')}"
                    id="categoryId" name="${spring.status.expression}" onchange="toggleFields()">
                <option value="">Выберите категорию</option>
                <#list categories as category>
                    <option value="${category.id}" ${(spring.stringStatusValue == category.id?string)?string("selected", "")}>${category.name}</option>
                </#list>
            </select>
            <@spring.showErrors "<br>", "error" />
        </div>

        <div class="form-group">
            <label for="description">Описание</label>
            <@spring.formInput "vacancyForm.description", "class='form-control' + ((spring.status.error && spring.status.errors['description']?has_content)?then(' error-field', ''))", "id='description'" />
            <@spring.showErrors "<br>" "error" />
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="salary">Зарплата</label>
                <@spring.formInput
                "vacancyForm.salary",
                "type='number' min='0' class='form-control' + ((spring.status.error && spring.status.errors['salary']?has_content)?then(' error-field', ''))",
                "id='salary'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="expFrom">Опыт от</label>
                <@spring.formInput
                "vacancyForm.expFrom",
                "type='number' min='0' class='form-control' + ((spring.status.error && spring.status.errors['expFrom']?has_content)?then(' error-field', ''))",
                "id='expFrom'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="expTo">Опыт до</label>
                <@spring.formInput
                "vacancyForm.expTo",
                "type='number' min='0' class='form-control' + ((spring.status.error && spring.status.errors['expTo']?has_content)?then(' error-field', ''))",
                "id='expTo'" />
                <@spring.showErrors "<br>" "error" />
            </div>
        </div>

        <div class="form-group">
            <label for="isActive">Статус вакансии</label>
            <@spring.formSingleSelect "vacancyForm.isActive", {"true": "Активно", "false": "Неактивно"}, "class='form-control' id='isActive' required='required'" />
            <@spring.showErrors "<br>", "error" />
        </div>

        <button type="submit" class="submit-btn">Создать вакансию</button>
    </form>
</@layout.page>