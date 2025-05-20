document.addEventListener('DOMContentLoaded', function() {
    const workExperiencesContainer = document.getElementById('work-experiences');
    const educationInfosContainer = document.getElementById('education-infos');
    const form = document.querySelector('.vacancy-form');

    if (typeof bootstrap !== 'undefined' && bootstrap.Tooltip) {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }

    function getWorkExperienceIndex() {
        const workExperiences = workExperiencesContainer.querySelectorAll('.work-experience-item');
        return workExperiences.length;
    }

    function getEducationIndex() {
        const educationItems = educationInfosContainer.querySelectorAll('.education-item');
        return educationItems.length;
    }

    function addWorkExperience(e) {
        e.preventDefault();
        const index = getWorkExperienceIndex();

        const i18n = document.getElementById('i18n');

        const workExpItem = document.createElement('div');
        workExpItem.classList.add('work-experience-item', 'p-4', 'mb-4', 'rounded');
        workExpItem.innerHTML = `
        <div class="row g-3 mb-3">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="form-label">${i18n.dataset.companyLabel} *</label>
                    <input type="text" 
                        class="form-control" 
                        id="companyName${index}" 
                        name="workExperiences[${index}].companyName">
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="form-label">${i18n.dataset.positionLabel} *</label>
                    <input type="text" 
                        class="form-control" 
                        id="position${index}" 
                        name="workExperiences[${index}].position">
                </div>
            </div>
        </div>
        <div class="row g-3 mb-3">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="form-label">${i18n.dataset.yearsLabel} *</label>
                    <input type="number" 
                        min="0" 
                        max="50" 
                        class="form-control" 
                        id="years${index}" 
                        name="workExperiences[${index}].years">
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="form-label">${i18n.dataset.monthsLabel} *</label>
                    <input type="number" 
                        min="0" 
                        max="11" 
                        class="form-control" 
                        id="months${index}" 
                        name="workExperiences[${index}].months">
                </div>
            </div>
        </div>
        <div class="form-group mb-4">
            <label class="form-label">${i18n.dataset.responsibilitiesLabel} *</label>
            <textarea class="form-control" 
                id="responsibilities${index}" 
                name="workExperiences[${index}].responsibilities" 
                rows="3"></textarea>
        </div>
        <div class="d-flex justify-content-end">
            <button type="button" name="removeWorkExp" value="${index}"
                    class="remove-btn btn btn-danger">
                ${i18n.dataset.removeLabel}
            </button>
        </div>
    `;

        workExpItem.querySelector('.remove-btn').addEventListener('click', function () {
            removeWorkExperience(workExpItem);
        });

        workExperiencesContainer.appendChild(workExpItem);
        updateEmptySectionMessages();
    }

    function addEducationItem(e) {
        e.preventDefault();
        const index = getEducationIndex();

        const i18n = document.getElementById('i18n');

        const educationItem = document.createElement('div');
        educationItem.classList.add('education-item', 'p-4', 'mb-4', 'rounded');
        educationItem.innerHTML = `
        <div class="row g-3 mb-3">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="form-label">${i18n.dataset.eduInstitution} *</label>
                    <input type="text" 
                        class="form-control" 
                        id="institution${index}" 
                        name="educationInfos[${index}].institution">
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="form-label">${i18n.dataset.eduDegree} *</label>
                    <input type="text" 
                        class="form-control" 
                        id="degree${index}" 
                        name="educationInfos[${index}].degree">
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="form-label">${i18n.dataset.eduProgram} *</label>
                    <input type="text" 
                        class="form-control" 
                        id="program${index}" 
                        name="educationInfos[${index}].program">
                </div>
            </div>
        </div>
        <div class="row g-3 mb-4">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="form-label">${i18n.dataset.eduStartDate} *</label>
                    <input type="date" 
                        class="form-control" 
                        id="startDate${index}" 
                        name="educationInfos[${index}].startDate">
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="form-label">${i18n.dataset.eduEndDate}</label>
                    <input type="date" 
                        class="form-control" 
                        id="endDate${index}" 
                        name="educationInfos[${index}].endDate">
                </div>
            </div>
        </div>
        <div class="d-flex justify-content-end">
            <button type="button" name="removeEducation" value="${index}" class="remove-btn btn btn-danger">
                ${i18n.dataset.eduRemove}
            </button>
        </div>
    `;

        educationItem.querySelector('.remove-btn').addEventListener('click', function () {
            removeEducation(educationItem);
        });

        educationInfosContainer.appendChild(educationItem);
    }

    function removeWorkExperience(item) {
        workExperiencesContainer.removeChild(item);
        reindexWorkExperiences();
        updateEmptySectionMessages();
    }

    function removeEducation(item) {
        educationInfosContainer.removeChild(item);
        reindexEducation();
        updateEmptySectionMessages();
    }

    function reindexWorkExperiences() {
        const workExperiences = workExperiencesContainer.querySelectorAll('.work-experience-item');
        workExperiences.forEach((exp, index) => {
            const inputs = exp.querySelectorAll('input, textarea');
            inputs.forEach(input => {
                const oldName = input.name;
                if (oldName) {
                    input.name = oldName.replace(/workExperiences\[\d+\]/, `workExperiences[${index}]`);
                }
                const oldId = input.id;
                if (oldId) {
                    const newId = oldId.replace(/\d+$/, index);
                    input.id = newId;

                    const label = exp.querySelector(`label[for="${oldId}"]`);
                    if (label) {
                        label.setAttribute('for', newId);
                    }
                }
            });

            const removeBtn = exp.querySelector('.remove-btn');
            if (removeBtn) {
                removeBtn.value = index;
            }
        });
    }

    function reindexEducation() {
        const educationItems = educationInfosContainer.querySelectorAll('.education-item');
        educationItems.forEach((edu, index) => {
            const inputs = edu.querySelectorAll('input, textarea');
            inputs.forEach(input => {
                const oldName = input.name;
                if (oldName) {
                    input.name = oldName.replace(/educationInfos\[\d+\]/, `educationInfos[${index}]`);
                }

                const oldId = input.id;
                if (oldId) {
                    const newId = oldId.replace(/\d+$/, index);
                    input.id = newId;
                    const label = edu.querySelector(`label[for="${oldId}"]`);
                    if (label) {
                        label.setAttribute('for', newId);
                    }
                }
            });
            const removeBtn = edu.querySelector('.remove-btn');
            if (removeBtn) {
                removeBtn.value = index;
            }
        });
    }

    const addWorkExpBtn = document.querySelector('button[name="addWorkExp"]');
    const addEducationBtn = document.querySelector('button[name="addEducation"]');

    if (addWorkExpBtn) {
        addWorkExpBtn.addEventListener('click', addWorkExperience);
    }

    if (addEducationBtn) {
        addEducationBtn.addEventListener('click', addEducationItem);
    }

    document.querySelectorAll('.remove-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const container = this.closest('.work-experience-item') ||
                this.closest('.education-item');

            if (container.classList.contains('work-experience-item')) {
                removeWorkExperience(container);
            } else if (container.classList.contains('education-item')) {
                removeEducation(container);
            }
        });
    });

    function updateEmptySectionMessages() {
        const workExpItems = workExperiencesContainer.querySelectorAll('.work-experience-item');
        const workExpEmptyMsg = workExperiencesContainer.querySelector('p.text-muted');

        if (workExpEmptyMsg) {
            workExpEmptyMsg.style.display = workExpItems.length === 0 ? 'block' : 'none';
        }

        const eduItems = educationInfosContainer.querySelectorAll('.education-item');
        const eduEmptyMsg = educationInfosContainer.querySelector('p.text-muted');

        if (eduEmptyMsg) {
            eduEmptyMsg.style.display = eduItems.length === 0 ? 'block' : 'none';
        }
    }

    updateEmptySectionMessages();
});