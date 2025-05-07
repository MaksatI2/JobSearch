<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/user/editProfile.css" activeNav="profile">
    <h1 class="page-title">Редактирование профиля</h1>

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
                <label for="name">Имя</label>
                <@spring.formInput "editUserDTO.name", "class='form-control' + ((spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="surname">Фамилия</label>
                <@spring.formInput "editUserDTO.surname", "class='form-control' + ((spring.status.errors['surname']?has_content)?then(' error-field', ''))", "id='surname'" />
                <@spring.showErrors "<br>" "error" />
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="age">Возраст</label>
                <@spring.formInput "editUserDTO.age", "class='form-control' + ((spring.status.errors['age']?has_content)?then(' error-field', ''))", "id='age'" />
                <@spring.showErrors "<br>" "error" />
            </div>

            <div class="form-group">
                <label for="phoneNumber">Номер телефона</label>
                <@spring.formInput "editUserDTO.phoneNumber", "class='form-control' + ((spring.status.errors['phoneNumber']?has_content)?then(' error-field', ''))", "id='phoneNumber'" />
                <@spring.showErrors "<br>" "error" />
            </div>
        </div>

        <div class="button-group">
            <button type="submit" class="submit-btn">Сохранить изменения</button>
            <a href="/profile" class="btn btn-secondary">Отмена</a>
        </div>
    </form>
</@layout.page>
