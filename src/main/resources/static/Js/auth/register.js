function showForm(type) {
    const applicantBtn = document.querySelector('button[data-type="applicant"]');
    const employerBtn = document.querySelector('button[data-type="employer"]');
    const applicantForm = document.getElementById('form-applicant-block');
    const employerForm = document.getElementById('form-employer-block');

    if (type === 'applicant') {
        applicantBtn.classList.add('active');
        employerBtn.classList.remove('active');
        applicantForm.style.display = 'block';
        employerForm.style.display = 'none';
    } else {
        employerBtn.classList.add('active');
        applicantBtn.classList.remove('active');
        employerForm.style.display = 'block';
        applicantForm.style.display = 'none';
    }

    sessionStorage.setItem('registerFormType', type);
}

document.addEventListener('DOMContentLoaded', function () {
    const storedType = sessionStorage.getItem('registerFormType');
    const initialType = storedType || '${type!""}'  || 'applicant';
    showForm(initialType);
});