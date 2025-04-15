<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Редактирование аватарки профиля</title>
    <link rel="stylesheet" href="/static/css/editAvatar.css">
</head>
<body>
<header class="header">
    <div class="container">
        <nav class="navbar">
            <a href="/" class="logo">JobSearch</a>
            <div class="nav-links">
                <a href="/" class="nav-link">Главная</a>
                <a href="/vacancies" class="nav-link">Вакансии</a>
                <#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <a href="/resumes/allResumes" class="nav-link">Резюме</a>
                </#if>
                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <a href="/profile" class="nav-link active">Профиль</a>
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

<section class="edit-avatar-section">
    <div class="container">
        <h2>Сменить аватар</h2>

        <div class="current-avatar-container">
            <h3>Текущий аватар</h3>
            <div class="avatar-wrapper">
                <img src="${user.avatar}" alt="Аватар пользователя" class="current-avatar" onerror="this.src='/static/default-avatar/default_avatar.jpg'">
            </div>
        </div>

        <div class="upload-container">
            <h3>Загрузить новый аватар</h3>
            <form action="/profile/avatar" method="post" enctype="multipart/form-data" class="avatar-form">
                <input type="hidden" name="userId" value="${userId}">
                <div class="file-input-wrapper">
                    <span class="file-label">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="upload-icon">
                            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                            <polyline points="17 8 12 3 7 8"></polyline>
                            <line x1="12" y1="3" x2="12" y2="15"></line>
                        </svg>
                        Выберите файл изображения
                    </span>
                    <input type="file" name="file" accept="image/*" required class="file-input">
                </div>
                <button type="submit" class="btn-save-avatar">Сохранить аватар</button>
            </form>
        </div>

        <div class="avatar-requirements">
            <h4>Требования к изображению:</h4>
            <ul>
                <li>Максимальный размер файла: 5МБ</li>
                <li>Поддерживаемые форматы: JPG,JPEG</li>
                <li>Рекомендуемый размер: 300x300 пикселей</li>
            </ul>
        </div>
    </div>
</section>

<footer class="footer">
    <div class="container">
        <p>© 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>