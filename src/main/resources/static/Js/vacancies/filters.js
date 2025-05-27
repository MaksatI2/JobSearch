
function getCurrentUserEmail() {

    if (typeof userEmail !== 'undefined' && userEmail) {
        return userEmail;
    }
    const metaEmail = document.querySelector('meta[name="user-email"]');
    if (metaEmail && metaEmail.content) {
        return metaEmail.content;
    }
    const bodyEmail = document.body.getAttribute('data-user-email');
    if (bodyEmail) {
        return bodyEmail;
    }
    const hiddenEmail = document.querySelector('input[name="userEmail"]');
    if (hiddenEmail && hiddenEmail.value) {
        return hiddenEmail.value;
    }
    const emailElement = document.querySelector('[data-email], .user-email, #user-email');
    if (emailElement) {
        return emailElement.textContent || emailElement.value || emailElement.getAttribute('data-email');
    }
    return null;
}

function isUserAuthenticated() {
    return getCurrentUserEmail() !== null;
}

function getUserSpecificKey(baseKey) {
    if (!isUserAuthenticated()) {
        return `guest_${baseKey}`;
    }
    const userEmail = getCurrentUserEmail();
    return `${baseKey}_${userEmail}`;
}

function setUserStorage(key, value) {
    const userKey = getUserSpecificKey(key);
    if (isUserAuthenticated()) {
        localStorage.setItem(userKey, value);
    } else {
        sessionStorage.setItem(userKey, value);
    }
}

function getUserStorage(key) {
    const userKey = getUserSpecificKey(key);
    return isUserAuthenticated()
        ? localStorage.getItem(userKey)
        : sessionStorage.getItem(userKey);
}

function removeUserStorage(key) {
    const userKey = getUserSpecificKey(key);
    if (isUserAuthenticated()) {
        localStorage.removeItem(userKey);
    } else {
        sessionStorage.removeItem(userKey);
    }
}

function migrateGuestFiltersToUser() {
    if (!isUserAuthenticated()) return;

    const filterKeys = ['categoryId', 'regionId', 'sort'];
    filterKeys.forEach(key => {
        const guestKey = `guest_${key}`;
        const guestValue = sessionStorage.getItem(guestKey);

        if (guestValue) {
            setUserStorage(key, guestValue);
            sessionStorage.removeItem(guestKey);
        }
    });
}

function clearAllUserFilters() {
    const filterKeys = ['categoryId', 'regionId', 'sort'];
    filterKeys.forEach(key => {
        removeUserStorage(key);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    migrateGuestFiltersToUser();

    const filterForm = document.querySelector('.filter-form');
    const applyBtn = document.getElementById('apply-filters');

    if (applyBtn && filterForm) {
        applyBtn.addEventListener('click', () => {
            const category = document.getElementById('category');
            const region = document.getElementById('region');
            const sort = document.getElementById('sort');

            if (category) setUserStorage('categoryId', category.value);
            if (region) setUserStorage('regionId', region.value);
            if (sort) setUserStorage('sort', sort.value);

            filterForm.submit();
        });
    }
});

window.addEventListener('DOMContentLoaded', () => {
    const categorySelect = document.getElementById('category');
    const regionSelect = document.getElementById('region');
    const sortSelect = document.getElementById('sort');
    const searchParams = new URLSearchParams(window.location.search);

    let shouldRedirect = false;

    if (!searchParams.has('categoryId') && getUserStorage('categoryId')) {
        searchParams.set('categoryId', getUserStorage('categoryId'));
        shouldRedirect = true;
    }

    if (!searchParams.has('regionId') && getUserStorage('regionId')) {
        searchParams.set('regionId', getUserStorage('regionId'));
        shouldRedirect = true;
    }

    if (!searchParams.has('sort') && getUserStorage('sort')) {
        searchParams.set('sort', getUserStorage('sort'));
        shouldRedirect = true;
    }

    if (shouldRedirect) {
        window.location.href = `/vacancies?${searchParams.toString()}`;
        return;
    }

    if (categorySelect && getUserStorage('categoryId')) {
        categorySelect.value = getUserStorage('categoryId');
    }

    if (regionSelect && getUserStorage('regionId')) {
        regionSelect.value = getUserStorage('regionId');
    }

    if (sortSelect && getUserStorage('sort')) {
        sortSelect.value = getUserStorage('sort');
    }

    if (categorySelect) {
        categorySelect.addEventListener('change', () => {
            setUserStorage('categoryId', categorySelect.value);
        });
    }

    if (regionSelect) {
        regionSelect.addEventListener('change', () => {
            setUserStorage('regionId', regionSelect.value);
        });
    }

    if (sortSelect) {
        sortSelect.addEventListener('change', () => {
            setUserStorage('sort', sortSelect.value);
        });
    }

    const resetBtn = document.getElementById('reset-filters');
    if (resetBtn) {
        resetBtn.addEventListener('click', () => {
            clearAllUserFilters();
        });
    }

    document.querySelector('.filter-btn-reset')?.addEventListener('click', () => {
        clearAllUserFilters();
    });
});