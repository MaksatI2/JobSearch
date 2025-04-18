<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Доступ запрещён | JobSearch</title>
    <style>
        :root {
            --primary-color: #4f46e5;
            --error-color: #ef4444;
            --text-color: #374151;
            --bg-color: #f9fafb;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--bg-color);
            color: var(--text-color);
            line-height: 1.6;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            text-align: center;
        }

        .error-container {
            max-width: 600px;
            padding: 2rem;
            margin: 0 auto;
        }

        .error-icon {
            font-size: 5rem;
            color: var(--error-color);
            margin-bottom: 1.5rem;
        }

        h1 {
            font-size: 2.5rem;
            margin-bottom: 1rem;
            color: var(--error-color);
        }

        p {
            font-size: 1.2rem;
            margin-bottom: 2rem;
        }

        .error-code {
            background-color: #e5e7eb;
            padding: 0.25rem 0.75rem;
            border-radius: 0.25rem;
            font-family: monospace;
            font-size: 0.9rem;
        }

        .home-link {
            display: inline-block;
            background-color: var(--primary-color);
            color: white;
            text-decoration: none;
            padding: 0.75rem 1.5rem;
            border-radius: 0.375rem;
            font-weight: 500;
            transition: background-color 0.2s;
            margin-top: 1rem;
        }

        .home-link:hover {
            background-color: #4338ca;
        }

        .error-details {
            margin-top: 2rem;
            padding: 1rem;
            background-color: #f3f4f6;
            border-radius: 0.375rem;
            font-size: 0.9rem;
            text-align: left;
        }
    </style>
</head>
<body>
<div class="error-container">
    <div class="error-icon">⛔</div>
    <h1>Доступ запрещён</h1>
    <p>У вас нет прав для просмотра этой страницы.</p>
    <div class="error-code">Ошибка 403</div>

    <a href="/" class="home-link">Вернуться на главную</a>

    <#if error??>
        <div class="error-details">
            <p><strong>Дополнительная информация:</strong></p>
            <p>${error}</p>
        </div>
    </#if>
</div>
</body>
</html>
