function showForm(formType) {
    document.querySelectorAll('.nav-link[data-type]').forEach(btn => {
        btn.classList.toggle('active', btn.getAttribute('data-type') === formType);
    });

    document.getElementById('form-applicant-block').style.display =
        formType === 'applicant' ? 'block' : 'none';
    document.getElementById('form-employer-block').style.display =
        formType === 'employer' ? 'block' : 'none';

    localStorage.setItem('selectedFormType', formType);

    const url = new URL(window.location);
    url.searchParams.set('type', formType);
    window.history.replaceState({}, '', url);
}

document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const typeParam = urlParams.get('type');

    if (typeParam === 'employer' || typeParam === 'applicant') {
        showForm(typeParam);
    } else {
        showForm('applicant');
    }
});