<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Редактирование профиля</title>
    <link rel="stylesheet" href="/static/css/profile.css">
    <link rel="stylesheet" href="/static/css/editProfile.css">
</head>
<body>
<header class="header">
    <div class="container">
        <nav class="navbar">
            <a href="/" class="logo">JobSearch</a>
            <div class="nav-links">
                <a href="/" class="nav-link">Главная</a>
                <a href="/vacancies" class="nav-link active">Вакансии</a>
                <#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <a href="/resumes/allResumes" class="nav-link">Резюме</a>
                </#if>
                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <a href="/profile" class="nav-link">Профиль</a>
                    <form action="/logout" method="post" class="logout-form">
                        <button type="submit" class="logout-btn">Выйти</button>
                    </form>
                <#else>
                    <a href="/auth/login" class="nav-link">Войти</a>
                    <a href="/auth/register" class="nav-link register-btn">Регистрация</a>
                </#if>
            </div>
        </nav>
    </div>
</header>

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

<footer class="footer">
    <div class="container">
        <p>© 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>