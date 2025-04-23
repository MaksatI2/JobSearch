<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование резюме | JobSearch</title>
    <link rel="stylesheet" href="/static/css/resumes/editResume.css">
    <link rel="stylesheet" href="/static/css/header.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="icon" href="/static/images/logo-transparent.png" type="image/x-icon">
</head>
<body>
<header class="header">
    <nav class="navbar">
        <img src="/static/images/logo-transparent.png" alt="JobSearch" class="logo-img">
        <div class="nav-links">
            <a href="/" class="nav-link">Главная</a>
            <a href="/vacancies" class="nav-link">Вакансии</a>
            <a href="/resumes/allResumes" class="nav-link active">Резюме</a>
            <#if Session.SPRING_SECURITY_CONTEXT??>
                <a href="/profile" class="nav-link">Профиль</a>
                <#if _csrf??>
                    <form action="/logout" method="post">
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf'}"
                               value="${(_csrf.token)!'--no-token--'}"/>
                        <button type="submit" class="logout-btn" id="logout">Выйти</button>
                    </form>
                </#if>
            <#else>
                <a href="/auth/login" class="nav-link">Войти</a>
                <a href="/auth/register" class="nav-link register-btn">Регистрация</a>
            </#if>
        </div>
    </nav>
</header>

<main class="main">
    <div class="container">
        <h1 class="page-title">Редактирование резюме</h1>

        <@spring.bind "resumeForm" />
        <#if spring.status.error>
            <div class="global-error">
                <#list spring.status.errorMessages as error>
                    <div>${error}</div>
                </#list>
            </div>
        </#if>

        <form action="/resumes/${resumeId}/edit" method="post" class="resume-form">
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                       value="${(_csrf.token)!'csrf-token'}"/>
            </#if>
            <div class="section">
                <h2 class="section-title">Основная информация</h2>

                <div class="form-group">
                    <label for="name">Название резюме*</label>
                    <@spring.formInput "resumeForm.name", "class='form-control' id='name'" />
                    <@spring.showErrors "<br>", "error" />
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
                    <@spring.showErrors "<br>", "error" />
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="salary">Желаемая зарплата</label>
                        <@spring.formInput "resumeForm.salary", "type='number' min='0' step='0.01' class='form-control' id='salary'" />
                        <@spring.showErrors "<br>", "error" />
                    </div>
                    <div class="form-group">
                        <label for="isActive">Статус резюме</label>
                        <@spring.formSingleSelect "resumeForm.isActive", {"true": "Активно", "false": "Неактивно"}, "class='form-control' id='isActive' required='required'" />
                        <@spring.showErrors "<br>", "error" />
                    </div>
                </div>
            </div>

            <div class="section">
                <h2 class="section-title">Образование</h2>
                <#if resumeForm.educationInfos?? && resumeForm.educationInfos?size gt 0>
                    <#list resumeForm.educationInfos as edu>
                        <div class="education-item" data-index="${edu?index}">
                            <@spring.bind "resumeForm.educationInfos[${edu?index}]" />
                            <div class="form-row">
                                <div class="form-group">
                                    <label>Учебное заведение*</label>
                                    <@spring.formInput "resumeForm.educationInfos[${edu?index}].institution", "class='form-control'" />
                                    <@spring.showErrors "<br>", "error" />
                                </div>
                                <div class="form-group">
                                    <label>Степень</label>
                                    <@spring.formInput "resumeForm.educationInfos[${edu?index}].degree", "class='form-control'" />
                                    <@spring.showErrors "<br>", "error" />
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-group">
                                    <label>Программа/Специальность*</label>
                                    <@spring.formInput "resumeForm.educationInfos[${edu?index}].program", "class='form-control'" />
                                    <@spring.showErrors "<br>", "error" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Дата начала *</label>
                                <@spring.formInput "resumeForm.educationInfos[${edu?index}].startDate",
                                "class='form-control datepicker'", "date"/>
                                <@spring.showErrors "<br>", "error"/>
                            </div>

                            <div class="form-group">
                                <label>Дата окончания</label>
                                <@spring.formInput "resumeForm.educationInfos[${edu?index}].endDate",
                                "class='form-control datepicker'", "date"/>
                                <@spring.showErrors "<br>", "error"/>
                            </div>
                            <button type="submit" name="removeEducation" value="${edu?index}" class="remove-btn">Удалить</button>
                        </div>
                    </#list>
                <#else>
                    <p>Нет информации об образовании</p>
                </#if>
                <button type="submit" name="addEducation" value="true" class="add-btn">+ Добавить образование</button>
            </div>

            <div class="section">
                <h2 class="section-title">Опыт работы</h2>
                <#if resumeForm.workExperiences?? && resumeForm.workExperiences?size gt 0>
                    <#list resumeForm.workExperiences as exp>
                        <div class="experience-item" data-index="${exp?index}">
                            <@spring.bind "resumeForm.workExperiences[${exp?index}]" />
                            <div class="form-row">
                                <div class="form-group">
                                    <label>Компания*</label>
                                    <@spring.formInput "resumeForm.workExperiences[${exp?index}].companyName", "class='form-control'" />
                                    <@spring.showErrors "<br>", "error" />
                                </div>
                                <div class="form-group">
                                    <label>Должность*</label>
                                    <@spring.formInput "resumeForm.workExperiences[${exp?index}].position", "class='form-control'" />
                                    <@spring.showErrors "<br>", "error" />
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-group">
                                    <label>Годы опыта*</label>
                                    <@spring.formInput "resumeForm.workExperiences[${exp?index}].years", "type='number' min='0' step='0.1' class='form-control'" />
                                    <@spring.showErrors "<br>", "error" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Обязанности и достижения</label>
                                <@spring.formTextarea "resumeForm.workExperiences[${exp?index}].responsibilities", "class='form-control'" />
                                <@spring.showErrors "<br>", "error" />
                            </div>
                            <button type="submit" name="removeWorkExp" value="${exp?index}" class="remove-btn">Удалить</button>
                        </div>
                    </#list>
                <#else>
                    <p>Нет информации об опыте работы</p>
                </#if>
                <button type="submit" name="addWorkExp" value="true" class="add-btn">+ Добавить опыт работы</button>
            </div>

            <div id="contact-infos" class="contacts-grid">
                <#list resumeForm.contactInfos as contact>
                    <div class="contact-item">
                        <div class="contact-input">
                            <#if contact.typeId == 1>
                                <img src="/static/images/contact/viber-logo-svgrepo-com.svg" alt="Телефон" class="contact-icon" />
                                <input type="text"
                                       name="contactInfos[${contact?index}].value"
                                       class="form-control no-label"
                                       placeholder="996700123456"
                                       pattern="^996\d{9}$"
                                       title="Формат: 996700123456"
                                       value="${contact.value!''}" />
                                <input type="hidden" name="contactInfos[${contact?index}].typeId" value="1" />
                            <#elseif contact.typeId == 2>
                                <img src="/static/images/contact/gmail-icon-logo-svgrepo-com.svg" alt="Email" class="contact-icon" />
                                <input type="email"
                                       name="contactInfos[${contact?index}].value"
                                       class="form-control no-label"
                                       placeholder="example@domain.com"
                                       title="Введите корректный email"
                                       value="${contact.value!''}" />
                                <input type="hidden" name="contactInfos[${contact?index}].typeId" value="2" />
                            <#elseif contact.typeId == 3>
                                <img src="/static/images/contact/linkedin-icon-2-logo-svgrepo-com.svg" alt="LinkedIn" class="contact-icon" />
                                <input type="url"
                                       name="contactInfos[${contact?index}].value"
                                       class="form-control no-label"
                                       placeholder="https://linkedin.com/in/username"
                                       pattern="^https://(www\.)?linkedin\.com/in/.+"
                                       title="Формат: https://linkedin.com/in/username"
                                       value="${contact.value!''}" />
                                <input type="hidden" name="contactInfos[${contact?index}].typeId" value="3" />
                            <#elseif contact.typeId == 4>
                                <img src="/static/images/contact/github-icon-1-logo-svgrepo-com.svg" alt="GitHub" class="contact-icon" />
                                <input type="url"
                                       name="contactInfos[${contact?index}].value"
                                       class="form-control no-label"
                                       placeholder="https://github.com/username"
                                       pattern="^https://(www\.)?github\.com/.+"
                                       title="Формат: https://github.com/username"
                                       value="${contact.value!''}" />
                                <input type="hidden" name="contactInfos[${contact?index}].typeId" value="4" />
                            <#elseif contact.typeId == 5>
                                <img src="/static/images/contact/telegram-logo-svgrepo-com.svg" alt="Telegram" class="contact-icon" />
                                <input type="text"
                                       name="contactInfos[${contact?index}].value"
                                       class="form-control no-label"
                                       placeholder="@username или 996700123456"
                                       pattern="^@\w{5,}$|^996\d{9}$"
                                       title="Формат: @username или 996700123456"
                                       value="${contact.value!''}" />
                                <input type="hidden" name="contactInfos[${contact?index}].typeId" value="5" />
                            <#elseif contact.typeId == 6>
                                <img src="/static/images/contact/vue-9-logo-svgrepo-com.svg" alt="WebSite" class="contact-icon" />
                                <input type="url"
                                       name="contactInfos[${contact?index}].value"
                                       class="form-control no-label"
                                       placeholder="https://yourportfolio.com"
                                       pattern="^(https?://)?[\w.-]+\.[a-z]{2,6}.*$"
                                       title="Введите корректный URL сайта"
                                       value="${contact.value!''}" />
                                <input type="hidden" name="contactInfos[${contact?index}].typeId" value="6" />
                            </#if>
                        </div>
                        <@spring.showErrors "<br>", "field-error" />
                    </div>
                </#list>
            </div>

            <div class="form-actions">
                <button type="submit" class="submit-btn">Сохранить изменения</button>
                <a href="/profile" class="cancel-btn">Отмена</a>
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