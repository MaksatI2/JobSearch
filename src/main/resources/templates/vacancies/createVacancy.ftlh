<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page
styles="/static/css/vacancies/vacancies.css"
activeNav="vacancies">
    <div class="container py-4">
        <h1 class="profile-title mb-4"><@spring.message code="vacancies.creation.title"/></h1>

        <@spring.bind "vacancyForm" />
        <#if spring.status.error>
            <div class="global-error">
                <#list spring.status.errorMessages as error>
                    <div>${error}</div>
                </#list>
            </div>
        </#if>

        <div class="card shadow-sm border-0 rounded-3 mb-4">
            <div class="card-body p-4">
                <form action="/vacancies/create" method="post">
                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                               value="${(_csrf.token)!'csrf-token'}"/>
                    </#if>

                    <div class="mb-3">
                        <label for="name" class="form-label"><@spring.message code="vacancies.editing.vacancy.name"/></label>
                        <@spring.formInput "vacancyForm.name", "class='form-control' + ((spring.status.errors['name']?has_content)?then(' error-field', ''))", "id='name'" />
                        <@spring.showErrors "<br>" "error" />
                    </div>

                    <div class="mb-3">
                        <@spring.bind "vacancyForm.categoryId"/>
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

                    <div class="mb-3">
                        <label for="description" class="form-label"><@spring.message code="vacancies.editing.description"/></label>
                        <@spring.formInput "vacancyForm.description", "class='form-control' rows='4' + ((spring.status.errors['description']?has_content)?then(' error-field', ''))", "id='description'" />
                        <@spring.showErrors "<br>" "error" />
                    </div>

                    <div class="mb-3">
                        <label for="responsibilities" class="form-label"><@spring.message code="vacancies.editing.responsibilities"/></label>
                        <@spring.formInput "vacancyForm.responsibilities", "class='form-control' rows='4' + ((spring.status.errors['responsibilities']?has_content)?then(' error-field', ''))", "id='responsibilities'" />
                        <@spring.showErrors "<br>" "error" />
                        <div class="form-text"><@spring.message code="vacancies.editing.responsibilities.help"/></div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-4">
                            <label for="salary" class="form-label"><@spring.message code="vacancies.editing.salary"/></label>
                            <@spring.formInput
                            "vacancyForm.salary",
                            "type='number' min='0' class='form-control' + ((spring.status.errors['salary']?has_content)?then(' error-field', ''))",
                            "id='salary'" />
                            <@spring.showErrors "<br>" "error" />
                        </div>

                        <div class="col-md-4">
                            <label for="expFrom" class="form-label"><@spring.message code="vacancies.editing.exp.from"/></label>
                            <@spring.formInput
                            "vacancyForm.expFrom",
                            "type='number' min='0' class='form-control' + ((spring.status.errors['expFrom']?has_content)?then(' error-field', ''))",
                            "id='expFrom'" />
                            <@spring.showErrors "<br>" "error" />
                        </div>

                        <div class="col-md-4">
                            <label for="expTo" class="form-label"><@spring.message code="vacancies.editing.exp.to"/></label>
                            <@spring.formInput
                            "vacancyForm.expTo",
                            "type='number' min='0' class='form-control' + ((spring.status.errors['expTo']?has_content)?then(' error-field', ''))",
                            "id='expTo'" />
                            <@spring.showErrors "<br>" "error" />
                        </div>
                    </div>

                    <div class="mb-3">
                        <@spring.bind "vacancyForm.regionId"/>
                        <label for="regionId" class="form-label">
                            <@spring.message code="vacancies.region"/>
                        </label>
                        <select class="form-control ${spring.status.error?string('is-invalid', '')}"
                                id="regionId" name="${spring.status.expression}">
                            <#assign defaultRegionId = 1>
                            <#list regions as region>
                                <option value="${region.id}"
                                        <#if (spring.stringStatusValue?? && spring.stringStatusValue == region.id?string)
                                || (!spring.stringStatusValue?? && region.id == defaultRegionId)>
                                selected
                                        </#if>>
                                    ${region.name}
                                </option>
                            </#list>
                        </select>
                        <@spring.showErrors "<br>", "error" />
                    </div>

                    <div class="mb-3">
                        <@spring.bind "vacancyForm.workScheduleIds"/>
                        <label class="form-label"><@spring.message code="vacancies.work.schedules"/></label>
                        <div class="form-control ${spring.status.error?string('is-invalid', '')}">
                            <#list workSchedules as schedule>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox"
                                           name="${spring.status.expression}"
                                           value="${schedule.id}"
                                           id="schedule-${schedule.id}"
                                           <#if spring.status.value?? && spring.status.value?is_sequence && spring.status.value?seq_contains(schedule.id)>checked</#if>>
                                    <label class="form-check-label" for="schedule-${schedule.id}">
                                        ${schedule.name}
                                    </label>
                                </div>
                            </#list>
                        </div>
                        <@spring.showErrors "<br>", "error" />
                    </div>

                    <div class="mb-3">
                        <@spring.bind "vacancyForm.employmentTypeIds"/>
                        <label class="form-label"><@spring.message code="vacancies.employment.types"/></label>
                        <div class="form-control ${spring.status.error?string('is-invalid', '')}">
                            <#list employmentTypes as type>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox"
                                           name="${spring.status.expression}"
                                           value="${type.id}"
                                           id="type-${type.id}"
                                           <#if spring.status.value?? && spring.status.value?is_sequence && spring.status.value?seq_contains(type.id)>checked</#if>>
                                    <label class="form-check-label" for="type-${type.id}">
                                        ${type.name}
                                    </label>
                                </div>
                            </#list>
                        </div>
                        <@spring.showErrors "<br>", "error" />
                    </div>

                    <div class="mb-4">
                        <label for="isActive" class="form-label"><@spring.message code="vacancies.editing.status"/></label>
                        <select class="form-select" id="isActive" name="isActive">
                            <option value="true" <#if vacancyForm.isActive?? && vacancyForm.isActive == true>selected</#if>><@spring.message code="vacancies.status.active"/></option>
                            <option value="false" <#if vacancyForm.isActive?? && vacancyForm.isActive == false>selected</#if>><@spring.message code="vacancies.status.inactive"/></option>
                        </select>
                    </div>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary py-2"><@spring.message code="vacancies.creation.create"/></button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</@layout.page>