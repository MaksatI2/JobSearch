document.addEventListener('DOMContentLoaded', function() {
    const workExperiencesContainer = document.getElementById('work-experiences');
    const educationInfosContainer = document.getElementById('education-infos');
    const form = document.querySelector('.vacancy-form');

    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    });

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

        const workExpItem = document.createElement('div');
        workExpItem.classList.add('work-experience-item', 'card', 'mb-4', 'shadow-sm', 'border-0');
        workExpItem.setAttribute('data-index', index);
        workExpItem.innerHTML = `
            <div class="card-header bg-gradient d-flex justify-content-between align-items-center">
                <h5 class="mb-0 text-white">
                    <i class="bi bi-building me-2"></i>Опыт работы
                </h5>
                <span class="badge bg-light text-dark rounded-pill">Опыт #${index + 1}</span>
            </div>
            <div class="card-body p-4">
                <div class="row g-3 mb-3">
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="text" 
                                class="form-control" 
                                id="companyName${index}" 
                                name="workExperiences[${index}].companyName" 
                                placeholder="Название компании">
                            <label for="companyName${index}">Компания *</label>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="text" 
                                class="form-control" 
                                id="position${index}" 
                                name="workExperiences[${index}].position" 
                                placeholder="Должность">
                            <label for="position${index}">Должность *</label>
                        </div>
                    </div>
                </div>
                
                <div class="row g-3 mb-3">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="form-label">Продолжительность работы *</label>
                            <div class="input-group">
                                <div class="form-floating flex-grow-1">
                                    <input type="number" 
                                        min="0" 
                                        max="50" 
                                        class="form-control" 
                                        id="years${index}" 
                                        name="workExperiences[${index}].years" 
                                        placeholder="Годы">
                                    <label for="years${index}">Годы</label>
                                </div>
                                <div class="form-floating flex-grow-1">
                                    <input type="number" 
                                        min="0" 
                                        max="11" 
                                        class="form-control" 
                                        id="months${index}" 
                                        name="workExperiences[${index}].months" 
                                        placeholder="Месяцы">
                                    <label for="months${index}">Месяцы</label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="form-label">Период работы</label>
                            <div class="input-group mb-3">
                                <input type="date" class="form-control" placeholder="Дата начала">
                                <span class="input-group-text">—</span>
                                <input type="date" class="form-control" placeholder="Дата окончания">
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="form-floating mb-3">
                    <textarea class="form-control" 
                        id="responsibilities${index}" 
                        name="workExperiences[${index}].responsibilities" 
                        style="height: 120px" 
                        placeholder="Обязанности"></textarea>
                    <label for="responsibilities${index}">Обязанности *</label>
                </div>
                
                <div class="d-flex justify-content-end mt-3">
                    <button type="button" class="remove-btn btn btn-outline-danger">
                        <i class="bi bi-trash me-2"></i>Удалить
                    </button>
                </div>
            </div>
        `;

        workExperiencesContainer.appendChild(workExpItem);

        workExpItem.querySelector('.remove-btn').addEventListener('click', function() {
            removeWorkExperience(workExpItem);
        });

        updateEmptySectionMessages();
    }

    function addEducation(e) {
        e.preventDefault();
        const index = getEducationIndex();

        const educationItem = document.createElement('div');
        educationItem.classList.add('education-item', 'card', 'mb-4', 'shadow-sm', 'border-0');
        educationItem.setAttribute('data-index', index);
        educationItem.innerHTML = `
            <div class="card-header bg-gradient d-flex justify-content-between align-items-center">
                <h5 class="mb-0 text-white">
                    <i class="bi bi-book me-2"></i>Образование
                </h5>
                <span class="badge bg-light text-dark rounded-pill">Образование #${index + 1}</span>
            </div>
            <div class="card-body p-4">
                <div class="row g-3 mb-3">
                    <div class="col-md-4">
                        <div class="form-floating">
                            <input type="text" 
                                class="form-control" 
                                id="institution${index}" 
                                name="educationInfos[${index}].institution" 
                                placeholder="Учебное заведение">
                            <label for="institution${index}">Учебное заведение *</label>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-floating">
                            <input type="text" 
                                class="form-control" 
                                id="degree${index}" 
                                name="educationInfos[${index}].degree" 
                                placeholder="Степень">
                            <label for="degree${index}">Степень *</label>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-floating">
                            <input type="text" 
                                class="form-control" 
                                id="program${index}" 
                                name="educationInfos[${index}].program" 
                                placeholder="Программа">
                            <label for="program${index}">Программа *</label>
                        </div>
                    </div>
                </div>
                
                <div class="row g-3 mb-3">
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="date" 
                                class="form-control" 
                                id="startDate${index}" 
                                name="educationInfos[${index}].startDate">
                            <label for="startDate${index}">Дата начала *</label>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-floating">
                            <input type="date" 
                                class="form-control" 
                                id="endDate${index}" 
                                name="educationInfos[${index}].endDate">
                            <label for="endDate${index}">Дата окончания</label>
                        </div>
                    </div>
                </div>
                
                <div class="d-flex justify-content-end mt-3">
                    <button type="button" class="remove-btn btn btn-outline-danger">
                        <i class="bi bi-trash me-2"></i>Удалить
                    </button>
                </div>
            </div>
        `;

        educationInfosContainer.appendChild(educationItem);

        const currentEducationCheckbox = educationItem.querySelector(`#currentEducation${index}`);
        if (currentEducationCheckbox) {
            const endDateInput = educationItem.querySelector(`#endDate${index}`);
            currentEducationCheckbox.addEventListener('change', function() {
                if (this.checked) {
                    endDateInput.value = '';
                    endDateInput.disabled = true;
                } else {
                    endDateInput.disabled = false;
                }
            });
        }

        educationItem.querySelector('.remove-btn').addEventListener('click', function() {
            removeEducation(educationItem);
        });

        updateEmptySectionMessages();
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
            exp.setAttribute('data-index', index);

            exp.querySelector('.badge').textContent = `Опыт #${index + 1}`;
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
        });
    }

    function reindexEducation() {
        const educationItems = educationInfosContainer.querySelectorAll('.education-item');
        educationItems.forEach((edu, index) => {
            edu.setAttribute('data-index', index);
            edu.querySelector('.badge').textContent = `Образование #${index + 1}`;

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
        });
    }

    const addWorkExpBtn = document.querySelector('button[name="addWorkExp"]');
    const addEducationBtn = document.querySelector('button[name="addEducation"]');

    if (addWorkExpBtn) {
        addWorkExpBtn.addEventListener('click', addWorkExperience);
    }

    if (addEducationBtn) {
        addEducationBtn.addEventListener('click', addEducation);
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

    document.querySelectorAll('.education-item .form-check-input').forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            const itemContainer = this.closest('.education-item');
            const index = itemContainer.getAttribute('data-index');
            const endDateInput = itemContainer.querySelector(`#endDate${index}`);

            if (this.checked) {
                endDateInput.value = '';
                endDateInput.disabled = true;
            } else {
                endDateInput.disabled = false;
            }
        });
    });

    if (form) {
        form.addEventListener('submit', function(e) {
            let isValid = true;

            const requiredFields = form.querySelectorAll('input[required], textarea[required], select[required]');
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    field.classList.add('is-invalid');
                    isValid = false;
                } else {
                    field.classList.remove('is-invalid');
                }
            });

            if (!isValid) {
                e.preventDefault();
            }

            return isValid;
        });
    }

    function updateEmptySectionMessages() {
        const workExpItems = workExperiencesContainer.querySelectorAll('.work-experience-item');
        const workExpEmptyMsg = document.querySelector('.work-exp-empty-message');

        if (workExpEmptyMsg) {
            workExpEmptyMsg.style.display = workExpItems.length === 0 ? 'block' : 'none';
        }

        const eduItems = educationInfosContainer.querySelectorAll('.education-item');
        const eduEmptyMsg = document.querySelector('.edu-empty-message');

        if (eduEmptyMsg) {
            eduEmptyMsg.style.display = eduItems.length === 0 ? 'block' : 'none';
        }
    }

    updateEmptySectionMessages();
});