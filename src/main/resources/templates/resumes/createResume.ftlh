<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/resumes/createResume.css" activeNav="resumes">
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
        <#if _csrf??>
            <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                   value="${(_csrf.token)!'csrf-token'}"/>
        </#if>
        <div class="form-group">
            <label for="name">Название резюме *</label>
            <@spring.formInput "resumeForm.name", "class='form-control' id='name'" />
            <@spring.showErrors "<br>", "error" />
        </div>


        <div class="form-group">
            <@spring.bind "resumeForm.categoryId"/>
            <select class="form-control"${spring.status.error?string('is-invalid', '')}"
            id="categoryId" name="${spring.status.expression}" onchange="toggleFields()">
            <option value="">Выберите категорию</option>
            <#list categories as category>
                <option value="${category.id}" ${(spring.stringStatusValue == category.id?string)?string("selected", "")}>${category.name}</option>
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
                                "type='number' min='0' max='50' class='form-control'" />
                                <@spring.showErrors "<br>", "field-error"/>
                            </div>
                            <div class="form-group">
                                <label>Количество месяцев *</label>
                                <@spring.formInput "resumeForm.workExperiences[${workExp?index}].months",
                                "type='number' min='0' max='11' class='form-control'" />
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
</@layout.page>