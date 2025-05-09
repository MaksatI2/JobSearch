<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/resumes/editResume.css" activeNav="resumes">
    <h1 class="page-title"><@spring.message code="resumes.edit.title"/></h1>

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
            <h2 class="section-title"><@spring.message code="resumes.edit.resume"/></h2>
            <div class="form-group">
                <label for="name"><@spring.message code="resumes.edit.name"/> *</label>
                <@spring.formInput "resumeForm.name", "class='form-control' id='name'" />
                <@spring.showErrors "<br>", "error" />
            </div>
            <div class="form-group">
                <label for="categoryId"><@spring.message code="resumes.edit.category"/> *</label>
                <select class="form-control" id="categoryId" name="categoryId">
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
                    <label for="salary"><@spring.message code="resumes.salary"/></label>
                    <@spring.formInput "resumeForm.salary", "type='number' min='0' step='0.01' class='form-control' id='salary'" />
                    <@spring.showErrors "<br>", "error" />
                </div>
                <div class="form-group">
                    <label for="isActive"><@spring.message code="resumes.create.status"/> </label>
                    <@spring.formSingleSelect "resumeForm.isActive", {
                    "true": " " + springMacroRequestContext.getMessage("resumes.create.status.active"),
                    "false": " " + springMacroRequestContext.getMessage("resumes.create.status.false")},
                    "class='form-control' id='isActive' required='required'" />
                    <@spring.showErrors "<br>", "error" />
                </div>
            </div>
        </div>

        <div class="section">
            <h2 class="section-title"><@spring.message code="resume.education.title"/></h2>
            <#if resumeForm.educationInfos?? && resumeForm.educationInfos?size gt 0>
                <#list resumeForm.educationInfos as edu>
                    <div class="education-item" data-index="${edu?index}">
                        <@spring.bind "resumeForm.educationInfos[${edu?index}]" />
                        <div class="form-row">
                            <div class="form-group">
                                <label><@spring.message code="resume.education.institution"/> *</label>
                                <@spring.formInput "resumeForm.educationInfos[${edu?index}].institution", "class='form-control'" />
                                <@spring.showErrors "<br>", "error" />
                            </div>
                            <div class="form-group">
                                <label><@spring.message code="resume.education.degree"/> *</label>
                                <@spring.formInput "resumeForm.educationInfos[${edu?index}].degree", "class='form-control'" />
                                <@spring.showErrors "<br>", "error" />
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label><@spring.message code="resume.education.program"/> *</label>
                                <@spring.formInput "resumeForm.educationInfos[${edu?index}].program", "class='form-control'" />
                                <@spring.showErrors "<br>", "error" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label><@spring.message code="resume.education.startDate"/> *</label>
                            <@spring.formInput "resumeForm.educationInfos[${edu?index}].startDate",
                            "class='form-control datepicker'", "date"/>
                            <@spring.showErrors "<br>", "error"/>
                        </div>
                        <div class="form-group">
                            <label><@spring.message code="resume.education.endDate"/></label>
                            <@spring.formInput "resumeForm.educationInfos[${edu?index}].endDate",
                            "class='form-control datepicker'", "date"/>
                            <@spring.showErrors "<br>", "error"/>
                        </div>
                        <button type="submit" name="removeEducation" value="${edu?index}" class="remove-btn">
                            <@spring.message code="resume.education.remove"/>
                        </button>
                    </div>
                </#list>
            <#else>
                <p><@spring.message code="resume.education.empty"/></p>
            </#if>
            <button type="submit" name="addEducation" value="true" class="add-btn">
                + <@spring.message code="resume.education.add"/>
            </button>
        </div>

        <div class="section">
            <h2 class="section-title"><@spring.message code="resume.work.title"/></h2>
            <#if resumeForm.workExperiences?? && resumeForm.workExperiences?size gt 0>
                <#list resumeForm.workExperiences as exp>
                    <div class="experience-item" data-index="${exp?index}">
                        <@spring.bind "resumeForm.workExperiences[${exp?index}]" />
                        <div class="form-row">
                            <div class="form-group">
                                <label><@spring.message code="resume.work.company"/> *</label>
                                <@spring.formInput "resumeForm.workExperiences[${exp?index}].companyName", "class='form-control'" />
                                <@spring.showErrors "<br>", "error" />
                            </div>
                            <div class="form-group">
                                <label><@spring.message code="resume.work.position"/> *</label>
                                <@spring.formInput "resumeForm.workExperiences[${exp?index}].position", "class='form-control'" />
                                <@spring.showErrors "<br>", "error" />
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label><@spring.message code="resume.work.years"/> *</label>
                                <@spring.formInput "resumeForm.workExperiences[${exp?index}].years",
                                "type='number' min='0' max='50' class='form-control'" />
                                <@spring.showErrors "<br>", "field-error"/>
                            </div>
                            <div class="form-group">
                                <label><@spring.message code="resume.work.months"/> *</label>
                                <@spring.formInput "resumeForm.workExperiences[${exp?index}].months",
                                "type='number' min='0' max='11' class='form-control'" />
                                <@spring.showErrors "<br>", "field-error"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label><@spring.message code="resume.work.responsibilities"/> *</label>
                            <@spring.formTextarea "resumeForm.workExperiences[${exp?index}].responsibilities", "class='form-control'" />
                            <@spring.showErrors "<br>", "error" />
                        </div>
                        <button type="submit" name="removeWorkExp" value="${exp?index}" class="remove-btn">
                            <@spring.message code="resume.work.remove"/>
                        </button>
                    </div>
                </#list>
            <#else>
                <p><@spring.message code="resume.work.empty"/></p>
            </#if>
            <button type="submit" name="addWorkExp" value="true" class="add-btn">
                + <@spring.message code="resume.work.add"/>
            </button>
        </div>

        <div id="contact-infos" class="contacts-grid">
            <#list resumeForm.contactInfos as contact>
                <div class="contact-item">
                    <div class="contact-input">
                        <#if contact.typeId == 1>
                            <img src="/static/images/contact/viber-logo-svgrepo-com.svg" alt="Phone" class="contact-icon" />
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
            <button type="submit" class="submit-btn"><@spring.message code="resume.create.submit"/></button>
            <a href="/profile" class="cancel-btn"><@spring.message code="resume.create.cancel"/></a>
        </div>
    </form>
</@layout.page>