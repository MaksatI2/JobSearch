<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создать резюме | JobSearch</title>
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
                <#if Session.SPRING_SECURITY_CONTEXT?? && Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <a href="/resumes/allResumes" class="nav-link active">Резюме</a>
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

<main class="main">
    <div class="container">
        <h1 class="page-title">Создать резюме</h1>

        <form action="/resumes/create" method="post" class="resume-form">
            <div class="form-section">
                <h2 class="section-title">Основная информация</h2>
                <div class="form-group">
                    <label for="name">Название резюме*</label>
                    <input type="text" class="form-control" id="name" name="name" value="${resumeForm.name!''}" required>
                    <#if fields?? && fields.hasErrors("name")>
                        <div class="error">${fields.getFieldError("name")?string!}</div>
                    </#if>
                </div>

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
                    <#if fields?? && fields.hasErrors("categoryId")>
                        <div class="error">${fields.getFieldError("categoryId")?string!}</div>
                    </#if>
                </div>

                <div class="form-group">
                    <label for="salary">Желаемая зарплата*</label>
                    <input type="number" class="form-control" id="salary" name="salary" value="${resumeForm.salary!''}">
                    <#if fields?? && fields.hasErrors("salary")>
                        <div class="error">${fields.getFieldError("salary")?string!}</div>
                    </#if>
                </div>
            </div>

            <div class="form-section">
                <h2 class="section-title">Опыт работы</h2>
                <#if workExpError??>
                    <div class="alert alert-danger">${workExpError}</div>
                </#if>
                <#list resumeForm.workExperiences![] as exp>
                    <div class="form-group form-group-nested">
                        <div class="form-group">
                            <label>Компания</label>
                            <input type="text" class="form-control" name="workExperiences[${exp_index}].companyName" value="${exp.companyName!}">
                        </div>
                        <div class="form-group">
                            <label>Должность</label>
                            <input type="text" class="form-control" name="workExperiences[${exp_index}].position" value="${exp.position!}">
                        </div>
                        <div class="form-group">
                            <label>Годы опыта</label>
                            <input type="number" class="form-control" name="workExperiences[${exp_index}].years" value="${exp.years!}">
                        </div>
                        <div class="form-group">
                            <label>Обязанности</label>
                            <textarea class="form-control" name="workExperiences[${exp_index}].responsibilities">${exp.responsibilities!}</textarea>
                        </div>
                    </div>
                </#list>
                <button type="submit" name="addWorkExp" class="btn btn-outline">Добавить опыт</button>
            </div>

            <div class="form-section">
                <h2 class="section-title">Образование</h2>
                <#if eduError??>
                    <div class="alert alert-danger">${eduError}</div>
                </#if>
                <#list resumeForm.educationInfos![] as edu>
                    <div class="form-group form-group-nested">
                        <div class="form-group">
                            <label>Учебное заведение</label>
                            <input type="text" class="form-control" name="educationInfos[${edu_index}].institution" value="${edu.institution!''}">
                        </div>
                        <div class="form-group">
                            <label>Программа</label>
                            <input type="text" class="form-control" name="educationInfos[${edu_index}].program" value="${edu.program!''}">
                        </div>
                        <div class="form-group">
                            <label>Степень</label>
                            <input type="text" class="form-control" name="educationInfos[${edu_index}].degree" value="${edu.degree!''}">
                        </div>
                        <div class="form-group">
                            <label>Дата начала (необязательно)</label>
                            <input type="date" class="form-control" name="educationInfos[${edu_index}].startDate"
                                   value="<#if edu.startDate??>${edu.startDate?string('yyyy-MM-dd')}</#if>">
                        </div>
                        <div class="form-group">
                            <label>Дата окончания (необязательно)</label>
                            <input type="date" class="form-control" name="educationInfos[${edu_index}].endDate"
                                   value="<#if edu.endDate??>${edu.endDate?string('yyyy-MM-dd')}</#if>">
                        </div>
                    </div>
                </#list>
                <button type="submit" name="addEducation" class="btn btn-outline">Добавить образование</button>
            </div>

            <div class="form-group">
                <label for="isActive">Статус резюме</label>
                <select class="form-control" id="isActive" name="isActive" required>
                    <option value="">Выберите статус</option>
                    <option value="true" <#if resumeForm.isActive?? && resumeForm.isActive == true>selected</#if>>Активно</option>
                    <option value="false" <#if resumeForm.isActive?? && resumeForm.isActive == false>selected</#if>>Неактивно</option>
                </select>
            </div>

            <div class="form-actions">
                <button type="submit" class="submit-btn">Создать резюме</button>
                <a href="/profile" class="btn btn-secondary">Отмена</a>
            </div>
        </form>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>© 2025 JobSearch. Все права защищены.</p>
    </div>
</footer>
</body>
</html>