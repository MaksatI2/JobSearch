<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Активные вакансии</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .vacancy-card {
            border: 1px solid #ccc;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 15px;
            background-color: #f9f9f9;
        }
        .vacancy-card h2 {
            margin-top: 0;
        }
        .vacancy-meta {
            font-size: 0.9em;
            color: #777;
        }
        .salary {
            font-weight: bold;
            color: #006400;
        }
    </style>
</head>
<body>
<h1>Список активных вакансий</h1>

<#if vacancies?? && vacancies?has_content>
    <#list vacancies as vacancy>
        <div class="vacancy-card">
            <h2>${vacancy.name}</h2>
            <p>${vacancy.description}</p>
            <p class="salary">Зарплата: ${vacancy.salary} руб.</p>
            <p>Опыт: от ${vacancy.expFrom} до ${vacancy.expTo} лет</p>
            <p class="vacancy-meta">Категория ID: ${vacancy.categoryId}, Автор ID: ${vacancy.authorId}</p>
            <p class="vacancy-meta">Дата обновления: ${vacancy.updateTime?string("dd.MM.yyyy HH:mm")}</p>
        </div>
    </#list>
<#else>
    <p>Нет доступных вакансий.</p>
</#if>

</body>
</html>
