<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page
styles="/static/css/vacancies/vacancies.css"
activeNav="vacancies">
    <h1 class="page-title"><@spring.message code="vacancies.title"/></h1>

    <#if successMessage??>
        <div class="alert alert-success">
            ${successMessage}
        </div>
    </#if>

    <#if errorMessage??>
        <div class="alert alert-danger">
            ${errorMessage}
        </div>
    </#if>

    <div class="unified-search-block">
        <div class="search-container">
            <input type="text" id="searchInput" placeholder="<@spring.message code="vacancies.search.placeholder"/>" class="search-input">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="search-icon">
                <circle cx="11" cy="11" r="8"></circle>
                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
            </svg>
            <div id="searchResults" class="search-results"></div>
        </div>

        <div class="filters-container">
            <form method="get" action="/vacancies" class="filter-form">
                <input type="hidden" name="page" value="0">
                <input type="hidden" name="size" value="${pageSize}">

                <div class="filter-group">
                    <select id="category" name="categoryId" class="form-control">
                        <option value=""><@spring.message code="category.all"/></option>
                        <#list categories as category>
                            <option value="${category.id}" <#if selectedCategory?? && selectedCategory == category.id>selected</#if>>
                                ${category.name}
                            </option>
                        </#list>
                    </select>
                </div>

                <div class="filter-group">
                    <select id="sort" name="sort" class="form-control">
                        <option value=""><@spring.message code="sort.date"/></option>
                        <option value="salaryAsc" <#if selectedSort?? && selectedSort == "salaryAsc">selected</#if>>
                            <@spring.message code="sort.salary.asc"/>
                        </option>
                        <option value="salaryDesc" <#if selectedSort?? && selectedSort == "salaryDesc">selected</#if>>
                            <@spring.message code="sort.salary.desc"/>
                        </option>
                        <option value="expAsc" <#if selectedSort?? && selectedSort == "expAsc">selected</#if>>
                            <@spring.message code="sort.exp.asc"/>
                        </option>
                        <option value="expDesc" <#if selectedSort?? && selectedSort == "expDesc">selected</#if>>
                            <@spring.message code="sort.exp.desc"/>
                        </option>
                        <option value="responsesDesc" <#if selectedSort?? && selectedSort == "responsesDesc">selected</#if>>
                            <@spring.message code="sort.responses.desc"/>
                        </option>
                    </select>
                </div>

                <div class="filter-actions">
                    <#if selectedSort?? || selectedCategory??>
                        <a href="/vacancies?page=0&size=${pageSize}" class="reset-btn">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" class="btn-icon">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
                            </svg>
                            <@spring.message code="filter.reset"/>
                        </a>
                    </#if>
                </div>
            </form>
        </div>
    </div>

    <#if vacancies?size == 0>
        <div class="empty-state">
            <h3><@spring.message code="vacancies.empty.title"/></h3>
            <p><@spring.message code="vacancies.empty.message"/></p>
        </div>
    </#if>

    <div class="vacancies-grid">
        <#list vacancies as vacancy>
            <div class="vacancy-card">
                <a href="/vacancies/${vacancy.id}/info" class="vacancy-card-link">
                    <div class="vacancy-header">
                        <h2 class="vacancy-title">${vacancy.name}</h2>
                        <div class="salary">${vacancy.salary} с</div>
                    </div>
                    <p class="vacancy-description">${vacancy.description}</p>
                    <div class="vacancy-meta">
                        <span class="experience">
                            <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                           <@spring.message code="vacancies.card.exp"/> ${vacancy.expFrom}-${vacancy.expTo}
                            <@spring.message code="vacancies.card.exp.years"/>
                        </span>
                        <span class="author">
                            <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                            </svg>
                            <@spring.message code="vacancies.card.author"/> ${vacancy.authorName!""}
                        </span>
                        <span class="update-time">
                            <svg class="icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                            </svg>
                             <@spring.message code="vacancies.card.date"/> ${vacancy.updateTime?string("dd.MM.yyyy")}
                        </span>

                        <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                            <form action="/favorites/vacancies/${vacancy.id}/add" method="post" class="favorite-form">
                                <input type="hidden" name="vacancyId" value="${vacancy.id}">
                                <#if _csrf??>
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                </#if>
                                <button type="submit" class="favorite-btn" title="Добавить в избранное">
                                    <svg width="24" height="24" viewBox="0 0 24 24"
                                         fill="<#if favoriteVacancyIds?? && favoriteVacancyIds?seq_contains(vacancy.id)>gold<#else>none</#if>"
                                         stroke="currentColor" stroke-width="2"
                                         stroke-linecap="round" stroke-linejoin="round" class="favorite-icon">
                                        <path d="M5 3v18l7-5 7 5V3z"/>
                                    </svg>
                                </button>
                            </form>
                        </#if>
                    </div>
                </a>
                <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                    <#if vacancy.authorId??>
                        <a href="/profile/viewEmployer/${vacancy.authorId}" class="company-profile-btn">
                            <@spring.message code="vacancies.company.profile"/>
                        </a>
                    </#if>
                    <a href="#modal-${vacancy.id}" class="respond-btn">
                        <@spring.message code="vacancies.respond.button"/>
                    </a>
                </#if>
            </div>

            <div id="modal-${vacancy.id}" class="modal">
                <div class="modal-content">
                    <a href="#" class="close-modal">&times;</a>
                    <h3 class="modal-title"><@spring.message code="vacancies.response.modal.title"/> "${vacancy.name}"</h3>

                    <form action="/response/respond" method="post">
                        <input type="hidden" name="vacancyId" value="${vacancy.id}">
                        <#if _csrf??>
                            <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>

                        <label for="resumeId-${vacancy.id}"><@spring.message code="vacancies.response.select.resume"/></label>
                        <select id="resumeId-${vacancy.id}" name="resumeId" class="resume-select" required>
                            <#if userResumes?? && userResumes?size gt 0>
                                <#list userResumes as resume>
                                    <option value="${resume.id}">${resume.name} (${resume.updateTime?string("dd.MM.yyyy")})</option>
                                </#list>
                            <#else>
                                <option value="" disabled><@spring.message code="vacancies.response.no.resumes"/></option>
                            </#if>
                        </select>

                        <button type="submit" class="submit-resume-btn">
                            <@spring.message code="vacancies.response.submit.button"/>
                        </button>
                    </form>
                </div>
            </div>
        </#list>
    </div>

    <#if totalPages gt 1>
        <div class="pagination">
            <#if currentPage gt 0>
                <a href="/vacancies?page=0&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                   class="page-link">««</a>
                <a href="/vacancies?page=${currentPage - 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                   class="page-link">«</a>
            </#if>

            <#list 0..totalPages-1 as i>
                <#if i == currentPage>
                    <span class="page-link active">${i + 1}</span>
                <#else>
                    <a href="/vacancies?page=${i}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                       class="page-link">${i + 1}</a>
                </#if>
            </#list>

            <#if currentPage lt totalPages - 1>
                <a href="/vacancies?page=${currentPage + 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                   class="page-link">»</a>
                <a href="/vacancies?page=${totalPages - 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>"
                   class="page-link">»»</a>
            </#if>

            <div class="page-size">
                <@spring.message code="pagination"/>
                <select onchange="window.location.href='/vacancies?page=0&size='+this.value+'<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedSort??>&sort=${selectedSort}</#if>'"
                        class="page-size-select">
                    <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                    <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                    <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                    <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                </select>
            </div>
        </div>
    </#if>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const categorySelect = document.getElementById('category');
            const sortSelect = document.getElementById('sort');
            const form = document.querySelector('.filter-form');

            [categorySelect, sortSelect].forEach(select => {
                select.addEventListener('change', function() {
                    form.submit();
                });
            });
        });
    </script>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const searchInput = document.getElementById('searchInput');
            const searchResults = document.getElementById('searchResults');
            const searchIcon = document.querySelector('.search-icon');
            let debounceTimer;

            searchInput.addEventListener('input', function(e) {
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

            searchIcon.addEventListener('click', function() {
                const query = searchInput.value.trim();
                if (query.length >= 2) {
                    fetchVacancies(query);
                } else {
                    searchInput.focus();
                }
            });

            function fetchVacancies(query) {
                console.log("Starting search with query:", query);
                searchResults.innerHTML = '<div class="search-result-item">Поиск...</div>';
                searchResults.classList.add('active');
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
                        searchResults.innerHTML = '<div class="search-result-item">Error: ' + error.message + '</div>';
                        searchResults.style.display = 'block';
                    });
            }

            function displayResults(vacancies) {
                if (vacancies.length === 0) {
                    searchResults.innerHTML = '<div class="search-result-item"><@spring.message code="vacancies.not.found"/></div>';
                    searchResults.classList.add('active');
                    return;
                }

                let resultsHtml = '';
                for (let i = 0; i < vacancies.length; i++) {
                    const vacancy = vacancies[i];
                    resultsHtml += '<div class="search-result-item" onclick="window.location.href=\'/vacancies/' +
                        vacancy.id + '/info\'"><strong>' + vacancy.name + '</strong><br></div>';
                }
                searchResults.innerHTML = resultsHtml;
                searchResults.classList.add('active');
            }

            document.addEventListener('click', function(e) {
                if (!searchInput.contains(e.target) && !searchIcon.contains(e.target) && !searchResults.contains(e.target)) {
                    searchResults.style.display = 'none';
                }
            });
            searchInput.focus();
        });
    </script>
</@layout.page>