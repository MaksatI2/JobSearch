<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создание резюме | JobSearch</title>
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
            <h1 class="page-title">Создание резюме</h1>

            <@spring.bind "resumeForm" />
            <#if spring.status.error>
                <div class="global-error">
                    <#list spring.status.errorMessages as error>
                        <div>${error}</div>
                    </#list>
                </div>
            </#if>

            <form action="/resumes/create" method="post" class="resume-form">
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
                    <@spring.showErrors "<br>" "error" />
                </div>

                <div class="form-group">
                    <label for="name">Название резюме *</label>
                    <@spring.formInput "resumeForm.name", "class='form-control' + ((spring.status.error && spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
                    <@spring.showErrors "<br>" "error" />
                </div>

                <div class="form-group">
                    <label for="salary">Желаемая зарплата</label>
                    <@spring.formInput "resumeForm.salary", "class='form-control' + ((spring.status.error && spring.status.errors['salary']?has_content)?then(' error-field', ''))", "id='salary' type='number' min='0' step='0.01'" />
                    <@spring.showErrors "<br>" "error" />
                </div>

                <div class="form-group">
                    <label for="description">Описание</label>
                    <textarea id="description" name="description" class="form-control" rows="5"><#if resumeForm.description??>${resumeForm.description}</#if></textarea>
                </div>

                <h3 class="section-title">Опыт работы</h3>
                <div id="work-experiences">
                    <#if resumeForm.workExperiences?? && resumeForm.workExperiences?has_content>
                        <#list resumeForm.workExperiences as workExp>
                            <@spring.bind "resumeForm.workExperiences[${workExp?index}]" />
                            <div class="work-experience-item">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Компания*</label>
                                        <input type="text" name="workExperiences[${workExp?index}].companyName" value="${(workExp.companyName!'')}" class="form-control${spring.status.error?then(' error-field', '')}" />
                                        <@spring.showErrors "<br>" "error" />
                                    </div>
                                    <div class="form-group">
                                        <label>Должность*</label>
                                        <input type="text" name="workExperiences[${workExp?index}].position" value="${(workExp.position!'')}" class="form-control${spring.status.error?then(' error-field', '')}" />
                                        <@spring.showErrors "<br>" "error" />
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Дата начала*</label>
                                        <input type="date" name="workExperiences[${workExp?index}].startDate" value="${(workExp.startDate??)?then(workExp.startDate?string('yyyy-MM-dd'), '')}" class="form-control${spring.status.error?then(' error-field', '')}" />
                                        <@spring.showErrors "<br>" "error" />
                                    </div>
                                    <div class="form-group">
                                        <label>Дата окончания</label>
                                        <input type="date" name="workExperiences[${workExp?index}].endDate" value="${(workExp.endDate??)?then(workExp.endDate?string('yyyy-MM-dd'), '')}" class="form-control${spring.status.error?then(' error-field', '')}" />
                                        <@spring.showErrors "<br>" "error" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label>Описание</label>
                                    <textarea name="workExperiences[${workExp?index}].description" class="form-control" rows="3">${(workExp.description!'')}</textarea>
                                    <@spring.showErrors "<br>" "error" />
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <p>Опыт работы не добавлен. Нажмите кнопку ниже, чтобы добавить.</p>
                    </#if>
                </div>
                <button type="submit" name="addWorkExp" class="btn-add">+ Добавить место работы</button>

                <h3 class="section-title">Образование</h3>
                <div id="education-infos">
                    <#if resumeForm.educationInfos?? && resumeForm.educationInfos?has_content>
                        <#list resumeForm.educationInfos as education>
                            <@spring.bind "resumeForm.educationInfos[${education?index}]" />
                            <div class="education-item">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Учебное заведение*</label>
                                        <input type="text" name="educationInfos[${education?index}].institution" value="${(education.institution!'')}" class="form-control${spring.status.error?then(' error-field', '')}" />
                                        <@spring.showErrors "<br>" "error" />
                                    </div>
                                    <div class="form-group">
                                        <label>Степень*</label>
                                        <input type="text" name="educationInfos[${education?index}].degree" value="${(education.degree!'')}" class="form-control${spring.status.error?then(' error-field', '')}" />
                                        <@spring.showErrors "<br>" "error" />
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Дата начала*</label>
                                        <input type="date" name="educationInfos[${education?index}].startDate" value="${(education.startDate??)?then(education.startDate?string('yyyy-MM-dd'), '')}" class="form-control${spring.status.error?then(' error-field', '')}" />
                                        <@spring.showErrors "<br>" "error" />
                                    </div>
                                    <div class="form-group">
                                        <label>Дата окончания</label>
                                        <input type="date" name="educationInfos[${education?index}].endDate" value="${(education.endDate??)?then(education.endDate?string('yyyy-MM-dd'), '')}" class="form-control${spring.status.error?then(' error-field', '')}" />
                                        <@spring.showErrors "<br>" "error" />
                                    </div>
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <p>Образование не добавлено. Нажмите кнопку ниже, чтобы добавить.</p>
                    </#if>
                </div>
                <button type="submit" name="addEducation" class="btn-add">+ Добавить образование</button>

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
                    <a href="/profile" class="cancel-btn">Отмена</a>
                </div>
            </form>
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