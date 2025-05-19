document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('searchInput');
    const searchResults = document.getElementById('searchResults');
    const searchIcon = document.querySelector('.search-icon');
    let debounceTimer;

    searchInput.addEventListener('focus', function() {
        this.parentElement.classList.add('active');
    });

    searchInput.addEventListener('blur', function() {
        setTimeout(() => {
            if (!searchResults.contains(document.activeElement)) {
                this.parentElement.classList.remove('active');
            }
        }, 200);
    });

    searchInput.addEventListener('input', function (e) {
        clearTimeout(debounceTimer);

        const query = e.target.value.trim();

        if (query.length < 2) {
            searchResults.style.display = 'none';
            return;
        }

        debounceTimer = setTimeout(() => {
            fetchVacancies(query);
        }, 300);
    });

    searchIcon.addEventListener('click', function () {
        const query = searchInput.value.trim();
        if (query.length >= 2) {
            fetchVacancies(query);
        } else {
            searchInput.focus();
        }
    });

    function fetchVacancies(query) {
        console.log("Starting search with query:", query);
        searchResults.innerHTML = '<div class="search-result-item"><i class="bi bi-hourglass-split me-2"></i>Поиск...</div>';
        searchResults.style.display = 'block';

        fetch('/vacancies/search?query=' + encodeURIComponent(query) + '&limit=10')
            .then(response => {
                console.log("Response status:", response.status);
                return response.json();
            })
            .then(vacancies => {
                console.log('Received vacancies:', vacancies);
                displayResults(vacancies);
            })
            .catch(error => {
                console.error('Full error:', error);
                searchResults.innerHTML = '<div class="search-result-item"><i class="bi bi-exclamation-triangle me-2"></i>Error: ' + error.message + '</div>';
                searchResults.style.display = 'block';
            });
    }

    function displayResults(vacancies) {
        if (vacancies.length === 0) {
            searchResults.innerHTML = `<div class="search-result-item"><i class="bi bi-search me-2"></i>${messages.notFound}</div>`;
            return;
        }

        let resultsHtml = '';
        for (let i = 0; i < vacancies.length; i++) {
            const vacancy = vacancies[i];
            resultsHtml += `
                <div class="search-result-item" onclick="window.location.href='/vacancies/${vacancy.id}/info'">
                    <div class="d-flex justify-content-between align-items-center">
                        <strong>${vacancy.name}</strong>
                        <span class="badge bg-primary rounded-pill">${vacancy.salary} с</span>
                    </div>
                    <small class="text-muted">${vacancy.description.substring(0, 60)}${vacancy.description.length > 60 ? '...' : ''}</small>
                </div>`;
        }
        searchResults.innerHTML = resultsHtml;
    }

    document.addEventListener('click', function (e) {
        if (!searchInput.contains(e.target) && !searchIcon.contains(e.target) && !searchResults.contains(e.target)) {
            searchResults.style.display = 'none';
        }
    });

    searchInput.focus();

    const categorySelect = document.getElementById('category');
    const sortSelect = document.getElementById('sort');
    const form = document.querySelector('.filter-form');

    [categorySelect, sortSelect].forEach(select => {
        select.addEventListener('change', function () {
            form.submit();
        });
    });
});