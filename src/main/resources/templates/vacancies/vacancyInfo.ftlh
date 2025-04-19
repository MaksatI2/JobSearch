<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Детали вакансии | JobSearch</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #4FA399;
            --primary-light: #6ABBB1;
            --primary-dark: #3A827A;
            --primary-extra-light: #E0F2F0;
            --dark: #2D3748;
            --gray: #718096;
            --light-gray: #EDF2F7;
            --white: #FFFFFF;
            --red: #E53E3E;
            --green: #38A169;
            --yellow: #D69E2E;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            color: var(--dark);
            background-color: #F7FAFC;
            line-height: 1.6;
        }

        .header {
            background-color: var(--primary);
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            position: sticky;
            top: 0;
            z-index: 100;
        }

        .navbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 20px;
            max-width: 1400px;
            margin: 0 auto;
        }

        .logo-img {
            height: 60px;
            width: auto;
            max-width: 200px;
            transition: transform 0.3s ease;
        }

        .logo-img:hover {
            transform: scale(1.05);
        }

        .nav-links {
            display: flex;
            align-items: center;
            gap: 25px;
        }

        .nav-link {
            color: var(--white);
            text-decoration: none;
            font-weight: 500;
            padding: 8px 12px;
            border-radius: 6px;
            transition: all 0.3s ease;
        }

        .nav-link:hover {
            background-color: rgba(255, 255, 255, 0.1);
        }

        .nav-link.active {
            background-color: rgba(255, 255, 255, 0.2);
            font-weight: 600;
        }

        .register-btn {
            background-color: var(--white);
            color: var(--primary);
            padding: 8px 20px;
            border-radius: 6px;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .register-btn:hover {
            background-color: var(--primary-extra-light);
            transform: translateY(-2px);
        }

        .logout-btn {
            background: none;
            border: none;
            color: var(--white);
            font-family: inherit;
            font-size: inherit;
            font-weight: 500;
            cursor: pointer;
            padding: 8px 12px;
            border-radius: 6px;
            transition: all 0.3s ease;
        }

        .logout-btn:hover {
            background-color: rgba(255, 255, 255, 0.1);
        }

        .main {
            padding: 40px 0;
            min-height: calc(100vh - 160px);
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .vacancy-detail {
            background-color: var(--white);
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
            padding: 30px;
            margin-bottom: 30px;
            border-left: 4px solid var(--primary);
        }

        .vacancy-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }

        .vacancy-title {
            font-size: 1.8rem;
            font-weight: 700;
            color: var(--dark);
            margin-right: 15px;
        }

        .salary {
            font-size: 1.8rem;
            font-weight: 700;
            color: var(--primary-dark);
            white-space: nowrap;
        }

        .vacancy-meta {
            display: flex;
            flex-wrap: wrap;
            gap: 25px;
            margin: 20px 0;
            font-size: 1rem;
            color: var(--gray);
            width: 100%;
        }

        .vacancy-meta span {
            display: flex;
            align-items: center;
        }

        .icon {
            margin-right: 8px;
            color: var(--primary);
        }

        .section {
            margin: 30px 0;
        }

        .section-title {
            font-size: 1.4rem;
            font-weight: 600;
            color: var(--primary-dark);
            margin-bottom: 15px;
            position: relative;
            padding-bottom: 8px;
        }

        .section-title::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            width: 60px;
            height: 3px;
            background-color: var(--primary);
            border-radius: 2px;
        }

        .section-content {
            color: var(--dark);
            line-height: 1.8;
            padding-left: 15px;
        }

        .actions {
            display: flex;
            gap: 15px;
            margin-top: 40px;
            flex-wrap: wrap;
        }

        .btn {
            display: inline-block;
            padding: 12px 24px;
            border-radius: 6px;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.3s ease;
            cursor: pointer;
            border: none;
            font-size: 1rem;
        }

        .btn-back {
            background-color: var(--gray);
            color: var(--white);
        }

        .btn-back:hover {
            background-color: #7f8c8d;
            transform: translateY(-2px);
        }

        .btn-edit {
            background-color: var(--yellow);
            color: var(--white);
        }

        .btn-edit:hover {
            background-color: #b7791f;
            transform: translateY(-2px);
        }

        .btn-refresh {
            background-color: var(--green);
            color: var(--white);
        }

        .btn-refresh:hover {
            background-color: #2d8559;
            transform: translateY(-2px);
        }

        .footer {
            padding: 25px 0;
            text-align: center;
            background-color: var(--primary);
            color: var(--white);
            font-size: 0.9rem;
        }

        .footer p {
            max-width: 1200px;
            margin: 0 auto;
        }

        @media (max-width: 768px) {
            .vacancy-header {
                flex-direction: column;
            }

            .salary {
                margin-top: 15px;
            }

            .vacancy-title, .salary {
                font-size: 1.5rem;
            }

            .actions {
                flex-direction: column;
            }

            .btn {
                width: 100%;
                text-align: center;
            }
        }
    </style>
</head>
<body>
<header class="header">
    <nav class="navbar">
        <a href="/" class="logo">
            <img src="/static/images/logo-transparent.png" alt="JobSearch" class="logo-img">
        </a>
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
</header>

<main class="main">
    <div class="container">
        <div class="vacancy-detail">
            <div class="vacancy-header">
                <h1 class="vacancy-title">${vacancy.name}</h1>
                <div class="salary">${vacancy.salary} с</div>
            </div>

            <div class="vacancy-meta">
                <span class="experience">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    Опыт: ${vacancy.expFrom}-${vacancy.expTo} лет
                </span>
                <span class="author">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                    Автор: ${vacancy.authorName?default("Неизвестно")}
                </span>
                <span class="update-time">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                    </svg>
                    Обновлена: ${vacancy.updateTime?string("dd.MM.yyyy")}
                </span>
                <span class="create-time">
                    <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    Опубликована: ${vacancy.createDate?string("dd.MM.yyyy")}
                </span>
            </div>

            <div class="section">
                <h2 class="section-title">Описание вакансии</h2>
                <div class="section-content">${vacancy.description}</div>
            </div>

            <div class="section">
                <h2 class="section-title">Требования</h2>
                <div class="section-content">
                    <p>Опыт работы: от ${vacancy.expFrom} до ${vacancy.expTo} лет</p>
                </div>
            </div>

            <div class="actions">
                <a href="/vacancies" class="btn btn-back">Назад к списку</a>
                <#if isAuthor>
                    <a href="/vacancies/${vacancy.id}/edit" class="btn btn-edit">Редактировать</a>
                    <form action="/vacancies/${vacancy.id}/refresh" method="post">
                        <button type="submit" class="btn btn-refresh">Обновить</button>
                    </form>
                </#if>
            </div>
        </div>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>© 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>