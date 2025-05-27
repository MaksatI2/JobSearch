<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/resumes/createResume.css" activeNav="resumes">
    <script src="/static/Js/resumes/create-resume.js"></script>
    <div class="container py-4">
        <h1 class="page-title mb-4"><@spring.message code="resume.create.title"/></h1>

        <div id="i18n"
             data-company-label="<@spring.message code='resume.work.company' />"
             data-position-label="<@spring.message code='resume.work.position' />"
             data-years-label="<@spring.message code='resume.work.years' />"
             data-months-label="<@spring.message code='resume.work.months' />"
             data-responsibilities-label="<@spring.message code='resume.work.responsibilities' />"
             data-remove-label="<@spring.message code='resume.work.remove' />"
             data-edu-institution="<@spring.message code='resume.education.institution' />"
             data-edu-degree="<@spring.message code='resume.education.degree' />"
             data-edu-program="<@spring.message code='resume.education.program' />"
             data-edu-start-date="<@spring.message code='resume.education.startDate' />"
             data-edu-end-date="<@spring.message code='resume.education.endDate' />"
             data-edu-remove="<@spring.message code='resume.education.remove' />">
        </div>

        <@spring.bind "resumeForm" />
        <#if spring.status.error>
            <div class="global-error alert alert-danger mb-4">
                <#list spring.status.errorMessages as error>
                    <div>${error}</div>
                </#list>
            </div>
        </#if>

        <form action="/resumes/create" method="post" class="vacancy-form bg-white p-4 rounded shadow">
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                       value="${(_csrf.token)!'csrf-token'}"/>
            </#if>

            <div class="form-group mb-4">
                <label for="name" class="form-label"><@spring.message code="resumes.create.name"/></label>
                <@spring.formInput "resumeForm.name", "class='form-control' id='name'" />
                <@spring.showErrors "<br>", "error" />
            </div>

            <div class="form-group mb-4">
                <@spring.bind "resumeForm.categoryId"/>
                <label for="categoryId" class="form-label"><@spring.message code="resumes.category"/></label>
                <select class="form-control ${spring.status.error?string('is-invalid', '')}"
                        id="categoryId" name="${spring.status.expression}" onchange="toggleFields()">
                    <option value=""><@spring.message code="resumes.category"/></option>
                    <#list categories as category>
                        <option value="${category.id}" ${(spring.stringStatusValue == category.id?string)?string("selected", "")}>${category.name}</option>
                    </#list>
                </select>
                <@spring.showErrors "<br>", "error" />
            </div>

            <div class="row mb-4">
                <div class="col-md-6 mb-3 mb-md-0">
                    <div class="form-group">
                        <label for="salary" class="form-label"><@spring.message code="resumes.salary"/></label>
                        <@spring.formInput "resumeForm.salary", "type='number' min='0' step='0.01' class='form-control' id='salary'" />
                        <@spring.showErrors "<br>", "error" />
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="isActive" class="form-label"><@spring.message code="resumes.create.status"/></label>
                        <@spring.formSingleSelect "resumeForm.isActive", {
                        "true": " " + springMacroRequestContext.getMessage("resumes.create.status.active"),
                        "false": " " + springMacroRequestContext.getMessage("resumes.create.status.false")},
                        "class='form-control' id='isActive' required='required'" />
                        <@spring.showErrors "<br>", "error" />
                    </div>
                </div>
            </div>
            <h3 class="section-title mb-3"><@spring.message code="resume.work.title"/></h3>
            <div id="work-experiences">
                <#if resumeForm.workExperiences?? && resumeForm.workExperiences?has_content>
                    <#list resumeForm.workExperiences as workExp>
                        <div class="work-experience-item p-4 mb-4 rounded">
                            <@spring.bind "resumeForm.workExperiences[${workExp?index}]" />
                            <#if spring.status.error>
                                <div class="alert alert-danger mb-3">
                                    <@spring.showErrors "<br/>"/>
                                </div>
                            </#if>

                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label"><@spring.message code="resume.work.company"/> *</label>
                                        <@spring.formInput "resumeForm.workExperiences[${workExp?index}].companyName", "class='form-control'" />
                                        <@spring.showErrors "<br>", "field-error"/>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label"><@spring.message code="resume.work.position"/> *</label>
                                        <@spring.formInput "resumeForm.workExperiences[${workExp?index}].position", "class='form-control'" />
                                        <@spring.showErrors "<br>", "field-error"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label"><@spring.message code="resume.work.years"/> *</label>
                                        <@spring.formInput "resumeForm.workExperiences[${workExp?index}].years",
                                        "type='number' class='form-control'" />
                                        <@spring.showErrors "<br>", "field-error"/>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label"><@spring.message code="resume.work.months"/> *</label>
                                        <@spring.formInput "resumeForm.workExperiences[${workExp?index}].months",
                                        "type='number' min='0' max='11' class='form-control'" />
                                        <@spring.showErrors "<br>", "field-error"/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group mb-4">
                                <label class="form-label"><@spring.message code="resume.work.responsibilities"/> *</label>
                                <@spring.formTextarea "resumeForm.workExperiences[${workExp?index}].responsibilities", "class='form-control' rows='3'" />
                                <@spring.showErrors "<br>", "field-error"/>
                            </div>
                            <div class="d-flex justify-content-end">
                                <button type="button" name="removeWorkExp" value="${workExp?index}"
                                        class="remove-btn btn btn-danger">
                                    <@spring.message code="resume.work.remove"/>
                                </button>
                            </div>
                        </div>
                    </#list>
                <#else>
                    <p class="text-muted mb-4"><@spring.message code="resume.work.empty"/></p>
                </#if>
            </div>

            <button type="button" name="addWorkExp" value="true" class="btn-add btn btn-primary mb-5">
                <i class="bi bi-plus me-2"></i><@spring.message code="resume.work.add"/>
            </button>

            <h3 class="section-title mb-3"><@spring.message code="resume.education.title"/></h3>
            <div id="education-infos">
                <#if resumeForm.educationInfos?? && resumeForm.educationInfos?has_content>
                    <#list resumeForm.educationInfos as education>
                        <div class="education-item p-4 mb-4 rounded">
                            <div class="row g-3 mb-3">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="form-label"><@spring.message code="resume.education.institution"/> *</label>
                                        <@spring.formInput "resumeForm.educationInfos[${education?index}].institution", "class='form-control'" />
                                        <@spring.showErrors "<br>", "field-error"/>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="form-label"><@spring.message code="resume.education.degree"/> *</label>
                                        <@spring.formInput "resumeForm.educationInfos[${education?index}].degree", "class='form-control'" />
                                        <@spring.showErrors "<br>", "field-error"/>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="form-label"><@spring.message code="resume.education.program"/> *</label>
                                        <@spring.formInput "resumeForm.educationInfos[${education?index}].program", "class='form-control'" />
                                        <@spring.showErrors "<br>", "field-error"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row g-3 mb-4">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label"><@spring.message code="resume.education.startDate"/> *</label>
                                        <@spring.formInput "resumeForm.educationInfos[${education?index}].startDate",
                                        "class='form-control'", "date"/>
                                        <@spring.showErrors "<br>", "field-error"/>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label"><@spring.message code="resume.education.endDate"/></label>
                                        <@spring.formInput "resumeForm.educationInfos[${education?index}].endDate",
                                        "class='form-control'", "date"/>
                                        <@spring.showErrors "<br>", "field-error"/>
                                    </div>
                                </div>
                            </div>
                            <div class="d-flex justify-content-end">
                                <button type="button" name="removeEducation" value="${education?index}" class="remove-btn btn btn-danger">
                                    <@spring.message code="resume.education.remove"/>
                                </button>
                            </div>
                        </div>
                    </#list>
                <#else>
                    <p class="text-muted mb-4"><@spring.message code="resume.education.empty"/></p>
                </#if>

                <@spring.bind "resumeForm.educationInfos" />
                <@spring.showErrors "<br>", "error" />
            </div>

            <button type="submit" name="addEducation" value="true" class="btn-add btn btn-primary mb-5">
                <i class="bi bi-plus me-2"></i><@spring.message code="resume.education.add"/>
            </button>

            <h3 class="section-title mb-3"><@spring.message code="resume.contacts.title"/></h3>
            <div id="contact-infos" class="row row-cols-1 row-cols-md-2 g-3 mb-4">
                <div class="col">
                    <div class="contact-item">
                        <div class="contact-input input-group">
                            <span class="input-group-text bg-transparent border-0">
                                <img src="/static/images/contact/viber-logo-svgrepo-com.svg" alt="Phone" class="contact-icon" />
                            </span>
                            <input type="text"
                                   name="contactInfos[0].value"
                                   class="form-control no-label border-start-0"
                                   placeholder="996XXXXXXXXX"/>
                            <input type="hidden" name="contactInfos[0].typeId" value="1" />
                        </div>
                        <@spring.showErrors "<br>", "field-error" />
                    </div>
                </div>

                <div class="col">
                    <div class="contact-item">
                        <div class="contact-input input-group">
                            <span class="input-group-text bg-transparent border-0">
                                <img src="/static/images/contact/gmail-icon-logo-svgrepo-com.svg" alt="Email" class="contact-icon" />
                            </span>
                            <input type="email"
                                   name="contactInfos[1].value"
                                   class="form-control no-label border-start-0"
                                   placeholder="example@domain.com" />
                            <input type="hidden" name="contactInfos[1].typeId" value="2" />
                        </div>
                        <@spring.showErrors "<br>", "field-error" />
                    </div>
                </div>

                <div class="col">
                    <div class="contact-item">
                        <div class="contact-input input-group">
                            <span class="input-group-text bg-transparent border-0">
                                <img src="/static/images/contact/linkedin-icon-2-logo-svgrepo-com.svg" alt="LinkedIn" class="contact-icon" />
                            </span>
                            <input type="url"
                                   name="contactInfos[2].value"
                                   class="form-control no-label border-start-0"
                                   placeholder="https://linkedin.com/in/username"/>
                            <input type="hidden" name="contactInfos[2].typeId" value="3" />
                        </div>
                        <@spring.showErrors "<br>", "field-error" />
                    </div>
                </div>

                <div class="col">
                    <div class="contact-item">
                        <div class="contact-input input-group">
                            <span class="input-group-text bg-transparent border-0">
                                <img src="/static/images/contact/github-icon-1-logo-svgrepo-com.svg" alt="GitHub" class="contact-icon" />
                            </span>
                            <input type="url"
                                   name="contactInfos[3].value"
                                   class="form-control no-label border-start-0"
                                   placeholder="https://github.com/username" />
                            <input type="hidden" name="contactInfos[3].typeId" value="4" />
                        </div>
                        <@spring.showErrors "<br>", "field-error" />
                    </div>
                </div>

                <div class="col">
                    <div class="contact-item">
                        <div class="contact-input input-group">
                            <span class="input-group-text bg-transparent border-0">
                                <img src="/static/images/contact/telegram-logo-svgrepo-com.svg" alt="Telegram" class="contact-icon" />
                            </span>
                            <input type="text"
                                   name="contactInfos[4].value"
                                   class="form-control no-label border-start-0"
                                   placeholder="@username или 996XXXXXXXXX" />
                            <input type="hidden" name="contactInfos[4].typeId" value="5" />
                        </div>
                        <@spring.showErrors "<br>", "field-error" />
                    </div>
                </div>

                <div class="col">
                    <div class="contact-item">
                        <div class="contact-input input-group">
                            <span class="input-group-text bg-transparent border-0">
                                <img src="/static/images/contact/vue-9-logo-svgrepo-com.svg" alt="WebSite" class="contact-icon" />
                            </span>
                            <input type="url"
                                   name="contactInfos[5].value"
                                   class="form-control no-label border-start-0"
                                   placeholder="https://yourportfolio.com" />
                            <input type="hidden" name="contactInfos[5].typeId" value="6" />
                        </div>
                        <@spring.showErrors "<br>", "field-error" />
                    </div>
                </div>
            </div>

            <div class="form-actions d-flex gap-3">
                <button type="submit" class="submit-btn btn btn-primary flex-grow-1">
                    <@spring.message code="resume.create.submit"/>
                </button>
                <a href="/profile" class="cancel-btn btn btn-secondary flex-grow-1">
                    <@spring.message code="resume.create.cancel"/>
                </a>
            </div>
        </form>
    </div>
</@layout.page>