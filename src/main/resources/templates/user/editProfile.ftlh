<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Редактирование профиля</title>
    <link rel="stylesheet" href="/static/css/profile.css">
    <link rel="stylesheet" href="/static/css/editProfile.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-container">
        <div class="navbar-logo">
            <a href="/">JobSearch</a>
        </div>

        <div class="navbar-menu">
            <ul>
                <li><a href="/">Главная</a></li>
                <li><a href="/">Вакансии</a></li>
                <#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <li><a href="/resumes/allResumes">Резюме</a></li>
                </#if>
                <li><a href="/profile">Профиль</a></li>
                <li>
                    <form action="/logout" method="post" style="display: inline;">
                        <button type="submit" class="logout-button">Выйти</button>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <h2>Редактирование профиля</h2>

    <div class="profile-edit-container">
        <div class="profile-edit-form">
            <h3>Изменить данные профиля</h3>
            <form action="/profile/edit" method="post">
                <div class="form-group">
                    <label for="name">Имя</label>
                    <input type="text" id="name" name="name" value="${editUserDTO.name}" required>
                    <#if errors?? && errors.getFieldError('name')??>
                        <span class="error">${errors.getFieldError('name').defaultMessage}</span>
                    </#if>
                </div>

                <div class="form-group">
                    <label for="surname">Фамилия</label>
                    <input type="text" id="surname" name="surname" value="${editUserDTO.surname}" required>
                    <#if errors?? && errors.getFieldError('surname')??>
                        <span class="error">${errors.getFieldError('surname').defaultMessage}</span>
                    </#if>
                </div>

                <div class="form-group">
                    <label for="age">Возраст</label>
                    <input type="number" id="age" name="age" value="${editUserDTO.age?c}" required min="18" max="60">
                    <#if errors?? && errors.getFieldError('age')??>
                        <span class="error">${errors.getFieldError('age').defaultMessage}</span>
                    </#if>
                </div>

                <div class="form-group">
                    <label for="phoneNumber">Номер телефона</label>
                    <input type="text" id="phoneNumber" name="phoneNumber" value="${editUserDTO.phoneNumber}" required>
                    <#if errors?? && errors.getFieldError('phoneNumber')??>
                        <span class="error">${errors.getFieldError('phoneNumber').defaultMessage}</span>
                    </#if>
                </div>

                <div class="button-group">
                    <button type="submit" class="btn btn-success">Сохранить</button>
                    <a href="/profile" class="btn btn-secondary">Отмена</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>