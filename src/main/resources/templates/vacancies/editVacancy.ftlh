<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page
styles="/static/css/vacancies/vacancies.css"
activeNav="vacancies">

    <h1 class="page-title">Редактировать вакансию</h1>

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
            <label for="name">Название вакансии</label>
            <@spring.formInput "vacancyForm.name", "class='form-control' + ((spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
            <@spring.showErrors "<br>" "error" />
        </div>

        <div class="form-group">
            <label for="categoryId">Категория:</label>
            <select class="form-control" id="categoryId" name="categoryId">
                <#list categories as category>
                    <option value="${category.id}" <#if vacancyForm.categoryId == category.id>selected</#if>>${category.name}</option>
                </#list>
            </select>
            <@spring.showErrors "<br>" "error" />
        </div>

        <div class="form-group">
            <label for="description">Описание</label>
            <@spring.formInput "vacancyForm.description", "class='form-control' + ((spring.status.errors['description']?has_content)?then(' error-field', ''))", "id='description'" />
            <@spring.showErrors "<br>" "error" />
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="salary">Зарплата</label>
                <@spring.formInput
                "vacancyForm.salary",
                "type='number' min='0' class='form-control' + ((spring.status.errors['salary']?has_content)?then(' error-field', ''))",
                "id='salary'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="expFrom">Опыт от</label>
                <@spring.formInput
                "vacancyForm.expFrom",
                "type='number' min='0' class='form-control' + ((spring.status.errors['expFrom']?has_content)?then(' error-field', ''))",
                "id='expFrom'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="expTo">Опыт до</label>
                <@spring.formInput
                "vacancyForm.expTo",
                "type='number' min='0' class='form-control' + ((spring.status.errors['expTo']?has_content)?then(' error-field', ''))",
                "id='expTo'" />
                <@spring.showErrors "<br>" "error" />
            </div>
        </div>

        <div class="form-group">
            <label for="isActive">Статус вакансии</label>
            <select id="isActive" name="isActive" required>
                <option value="">Выберите статус</option>
                <option value="true" <#if vacancyForm.isActive?? && vacancyForm.isActive == true>selected</#if>>Активна</option>
                <option value="false" <#if vacancyForm.isActive?? && vacancyForm.isActive == false>selected</#if>>Неактивна</option>
            </select>
            <@spring.showErrors "isActive" "error" />
        </div>

        <button type="submit" class="submit-btn">Сохранить изменения</button>
    </form>
</@layout.page>