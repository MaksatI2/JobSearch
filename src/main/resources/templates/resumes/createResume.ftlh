<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создание резюме | JobSearch</title>
    <link rel="stylesheet" href="/static/css/resumes/createResume.css">
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
        <h1 class="page-title">Создание резюме</h1>

        <@spring.bind "resumeForm" />
        <#if spring.status.error>
            <div class="global-error">
                <#list spring.status.errorMessages as error>
                    <div>${error}</div>
                </#list>
            </div>
        </#if>

        <form action="/resumes/create" method="post" class="vacancy-form">
            <div class="form-group">
                <label for="name">Название резюме *</label>
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
            <h3 class="section-title">Опыт работы</h3>
            <div id="work-experiences">
                <#if resumeForm.workExperiences?? && resumeForm.workExperiences?has_content>
                    <#list resumeForm.workExperiences as workExp>
                        <div class="work-experience-item">
                            <@spring.bind "resumeForm.workExperiences[${workExp?index}]" />
                            <#if spring.status.error>
                                <div class="alert alert-danger">
                                    <@spring.showErrors "<br/>"/>
                                </div>
                            </#if>

                            <div class="form-row">
                                <div class="form-group">
                                    <label>Компания *</label>
                                    <@spring.formInput "resumeForm.workExperiences[${workExp?index}].companyName", "class='form-control'" />
                                    <@spring.showErrors "<br>", "field-error"/>
                                </div>
                                <div class="form-group">
                                    <label>Должность *</label>
                                    <@spring.formInput "resumeForm.workExperiences[${workExp?index}].position", "class='form-control'" />
                                    <@spring.showErrors "<br>", "field-error"/>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-group">
                                    <label>Количество лет работы *</label>
                                    <@spring.formInput "resumeForm.workExperiences[${workExp?index}].years",
                                    "type='number' min='0' step='0.1' class='form-control'" />
                                    <@spring.showErrors "<br>", "field-error"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Обязанности *</label>
                                <@spring.formTextarea "resumeForm.workExperiences[${workExp?index}].responsibilities", "class='form-control' rows='3'" />
                                <@spring.showErrors "<br>", "field-error"/>
                            </div>
                            <button type="submit" name="removeWorkExp" value="${workExp?index}" class="remove-btn">
                                Удалить опыт работы
                            </button>
                        </div>
                    </#list>
                <#else>
                    <p>Опыт работы не добавлен. Нажмите кнопку ниже, чтобы добавить.</p>
                </#if>
            </div>

            <#if !(resumeForm.workExperiences?? && resumeForm.workExperiences?has_content)>
                <button type="submit" name="addWorkExp" value="true" class="btn-add">Добавить место работы</button>
            </#if>

            <h3 class="section-title">Образование</h3>
            <div id="education-infos">
                <#if resumeForm.educationInfos?? && resumeForm.educationInfos?has_content>
                    <#list resumeForm.educationInfos as education>
                        <div class="education-item">
                            <div class="form-row">
                                <div class="form-group">
                                    <label>Учебное заведение *</label>
                                    <@spring.formInput "resumeForm.educationInfos[${education?index}].institution", "class='form-control'" />
                                    <@spring.showErrors "<br>", "field-error"/>
                                </div>
                                <div class="form-group">
                                    <label>Степень *</label>
                                    <@spring.formInput "resumeForm.educationInfos[${education?index}].degree", "class='form-control'" />
                                    <@spring.showErrors "<br>", "field-error"/>
                                </div>
                                <div class="form-group">
                                    <label>Программа *</label>
                                    <@spring.formInput "resumeForm.educationInfos[${education?index}].program", "class='form-control'" />
                                    <@spring.showErrors "<br>", "field-error"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Дата начала *</label>
                                <@spring.formInput "resumeForm.educationInfos[${education?index}].startDate",
                                "class='form-control datepicker'", "date"/>
                                <@spring.showErrors "<br>", "field-error"/>
                            </div>

                            <div class="form-group">
                                <label>Дата окончания</label>
                                <@spring.formInput "resumeForm.educationInfos[${education?index}].endDate",
                                "class='form-control datepicker'", "date"/>
                                <@spring.showErrors "<br>", "field-error"/>
                            </div>
                            <button type="submit" name="removeEducation" value="${education?index}" class="remove-btn">
                                Удалить образование
                            </button>
                        </div>
                    </#list>
                <#else>
                    <p>Образование не добавлено. Нажмите кнопку ниже, чтобы добавить.</p>
                </#if>

                <@spring.bind "resumeForm.educationInfos" />
                <@spring.showErrors "<br>", "error" />
            </div>

            <#if !(resumeForm.educationInfos?? && resumeForm.educationInfos?has_content)>
                <button type="submit" name="addEducation" value="true" class="btn-add">Добавить образование</button>
            </#if>

            <h3 class="section-title">Контактная информация</h3>
            <div id="contact-infos" class="contacts-grid">

                <div class="contact-item">
                    <div class="contact-input">
                        <img src="/static/images/contact/viber-logo-svgrepo-com.svg" alt="Телефон" class="contact-icon" />
                        <input type="text"
                               name="contactInfos[0].value"
                               class="form-control no-label"
                               placeholder="996XXXXXXXXX"
                               pattern="^996\d{9}$"
                               title="Формат: 996(XXX)XXXXXX" />
                        <input type="hidden" name="contactInfos[0].typeId" value="1" />
                    </div>
                    <@spring.showErrors "<br>", "field-error" />
                </div>

                <div class="contact-item">
                    <div class="contact-input">
                        <img src="/static/images/contact/gmail-icon-logo-svgrepo-com.svg" alt="Email" class="contact-icon" />
                        <input type="email"
                               name="contactInfos[1].value"
                               class="form-control no-label"
                               placeholder="example@domain.com"
                               title="Введите корректный email" />
                        <input type="hidden" name="contactInfos[1].typeId" value="2" />
                    </div>
                    <@spring.showErrors "<br>", "field-error" />
                </div>

                <div class="contact-item">
                    <div class="contact-input">
                        <img src="/static/images/contact/linkedin-icon-2-logo-svgrepo-com.svg" alt="LinkedIn" class="contact-icon" />
                        <input type="url"
                               name="contactInfos[2].value"
                               class="form-control no-label"
                               placeholder="https://linkedin.com/in/username"
                               pattern="^https://(www\.)?linkedin\.com/in/.+"
                               title="Формат: https://linkedin.com/in/username" />
                        <input type="hidden" name="contactInfos[2].typeId" value="3" />
                    </div>
                    <@spring.showErrors "<br>", "field-error" />
                </div>

                <div class="contact-item">
                    <div class="contact-input">
                        <img src="/static/images/contact/github-icon-1-logo-svgrepo-com.svg" alt="GitHub" class="contact-icon" />
                        <input type="url"
                               name="contactInfos[3].value"
                               class="form-control no-label"
                               placeholder="https://github.com/username"
                               pattern="^https://(www\.)?github\.com/.+"
                               title="Формат: https://github.com/username" />
                        <input type="hidden" name="contactInfos[3].typeId" value="4" />
                    </div>
                    <@spring.showErrors "<br>", "field-error" />
                </div>

                <div class="contact-item">
                    <div class="contact-input">
                        <img src="/static/images/contact/telegram-logo-svgrepo-com.svg" alt="Telegram" class="contact-icon" />
                        <input type="text"
                               name="contactInfos[4].value"
                               class="form-control no-label"
                               placeholder="@username или 996XXXXXXXXX"
                               pattern="^@\w{5,}$|^996\d{9}$"
                               title="Формат: @username или 996XXXXXXXXX" />
                        <input type="hidden" name="contactInfos[4].typeId" value="5" />
                        <@spring.showErrors "<br>", "field-error" />
                    </div>
                </div>

                <div class="contact-item">
                    <div class="contact-input">
<#--                        Да это vue, больше ничего интересного в голову не пришло)-->
                        <img src="/static/images/contact/vue-9-logo-svgrepo-com.svg" alt="WebSite" class="contact-icon" />
                        <input type="url"
                               name="contactInfos[5].value"
                               class="form-control no-label"
                               placeholder="https://yourportfolio.com"
                               pattern="^(https?://)?[\w.-]+\.[a-z]{2,6}.*$"
                               title="Введите корректный URL сайта" />
                        <input type="hidden" name="contactInfos[5].typeId" value="6" />
                        <@spring.showErrors "<br>", "field-error" />
                    </div>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="submit-btn">Создать резюме</button>
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