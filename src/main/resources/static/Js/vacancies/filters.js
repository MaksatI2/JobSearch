function setCookie(name, value, days = 7) {
    const expires = new Date(Date.now() + days * 864e5).toUTCString();
    document.cookie = name + '=' + encodeURIComponent(value) + '; expires=' + expires + '; path=/';
}

function getCookie(name) {
    return document.cookie.split('; ').reduce((r, v) => {
        const parts = v.split('=');
        return parts[0] === name ? decodeURIComponent(parts[1]) : r
    }, '');
}

window.addEventListener('DOMContentLoaded', () => {
    const categorySelect = document.getElementById('category');
    const sortSelect = document.getElementById('sort');
    const searchParams = new URLSearchParams(window.location.search);

    let shouldRedirect = false;

    if (!searchParams.has('categoryId') && getCookie('categoryId')) {
        searchParams.set('categoryId', getCookie('categoryId'));
        shouldRedirect = true;
    }

    if (!searchParams.has('sort') && getCookie('sort')) {
        searchParams.set('sort', getCookie('sort'));
        shouldRedirect = true;
    }

    if (shouldRedirect) {
        const newUrl = `${window.location.pathname}?${searchParams.toString()}`;
        window.location.replace(newUrl);
        return;
    }

    if (categorySelect) {
        categorySelect.addEventListener('change', () => {
            setCookie('categoryId', categorySelect.value);
        });
    }

    if (sortSelect) {
        sortSelect.addEventListener('change', () => {
            setCookie('sort', sortSelect.value);
        });
    }

    const pageSizeSelect = document.querySelector('.page-size-select');
    if (pageSizeSelect) {
        pageSizeSelect.addEventListener('change', () => {
        });
    }

    document.querySelectorAll('.page-link').forEach(link => {
        link.addEventListener('click', (e) => {
        });
    });

    const resetBtn = document.getElementById('reset-filters');
    if (resetBtn) {
        resetBtn.addEventListener('click', () => {
            setCookie('categoryId', '');
            setCookie('sort', '');
        });
    }
});
