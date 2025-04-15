<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование резюме | JobSearch</title>
    <link rel="stylesheet" href="/static/css/createResume.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
<header class="header">
    <div class="container">
        <nav class="navbar">
            <a href="/" class="logo">JobSearch</a>
            <div class="nav-links">
                <a href="/" class="nav-link">Главная</a>
                <a href="/vacancies" class="nav-link">Вакансии</a>
                <a href="/resumes/allResumes" class="nav-link">Резюме</a>
            </div>
        </nav>
    </div>
</header>

<main class="main">
    <div class="container">
        <div class="resume-form-container">
            <h1 class="page-title">Редактирование резюме</h1>

            <@spring.bind "resumeForm" />
            <#if spring.status.error>
                <div class="global-error">
                    <#list spring.status.errorMessages as error>
                        <div>${error}</div>
                    </#list>
                </div>
            </#if>

            <form method="post" class="resume-form" action="/resumes/${resumeId}/edit">
                <div class="form-group">
                    <label for="categoryId">Категория*</label>
                    <select class="form-control" id="categoryId" name="categoryId" required>
                        <option value="">Выберите категорию</option>
                        <#list categories as category>
                            <option value="${category.id}"
                                    <#if resumeForm.categoryId?? && resumeForm.categoryId == category.id>selected</#if>>
                                ${category.name}
                            </option>
                        </#list>
                    </select>
                    <@spring.showErrors "categoryId" "error" />
                </div>

                <!-- Work Experience Section -->
                <div class="section-title">Опыт работы</div>
                <#list resumeForm.workExperiences as workExp>
                    <div class="form-subsection">
                        <input type="hidden" name="workExperiences[${workExp?index}].id" value="${workExp.id!}">
                        <div class="form-group">
                            <label>Компания</label>
                            <input type="text" name="workExperiences[${workExp?index}].company" value="${workExp.company!}" required>
                        </div>
                        <div class="form-group">
                            <label>Должность</label>
                            <input type="text" name="workExperiences[${workExp?index}].position" value="${workExp.position!}" required>
                        </div>
                        <div class="form-group">
                            <label>Период</label>
                            <input type="date" name="workExperiences[${workExp?index}].startDate" value="${workExp.startDate?date?string("yyyy-MM-dd")!}">
                            <input type="date" name="workExperiences[${workExp?index}].endDate" value="${workExp.endDate?date?string("yyyy-MM-dd")!}">
                        </div>
                        <button type="submit" name="removeWorkExp" value="${workExp?index}" class="remove-btn">Удалить опыт</button>
                    </div>
                </#list>
                <button type="submit" name="addWorkExp" class="add-btn">Добавить опыт работы</button>

                <!-- Education Section -->
                <div class="section-title">Образование</div>
                <#list resumeForm.educationInfos as edu>
                    <div class="form-subsection">
                        <input type="hidden" name="educationInfos[${edu?index}].id" value="${edu.id!}">
                        <div class="form-group">
                            <label>Учебное заведение</label>
                            <input type="text" name="educationInfos[${edu?index}].institution" value="${edu.institution!}" required>
                        </div>
                        <div class="form-group">
                            <label>Специальность</label>
                            <input type="text" name="educationInfos[${edu?index}].major" value="${edu.major!}" required>
                        </div>
                        <div class="form-group">
                            <label>Период</label>
                            <input type="date" name="educationInfos[${edu?index}].startDate" value="${edu.startDate?date?string("yyyy-MM-dd")!}">
                            <input type="date" name="educationInfos[${edu?index}].endDate" value="${edu.endDate?date?string("yyyy-MM-dd")!}">
                        </div>
                        <button type="submit" name="removeEducation" value="${edu?index}" class="remove-btn">Удалить образование</button>
                    </div>
                </#list>
                <button type="submit" name="addEducation" class="add-btn">Добавить образование</button>

                <div class="form-actions">
                    <button type="submit" class="submit-btn">Сохранить изменения</button>
                    <a href="/profile" class="cancel-btn">Отмена</a>
                </div>
            </form>
        </div>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>&copy; 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>
