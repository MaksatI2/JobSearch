<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page
styles="/static/css/vacancies/vacancies.css"
activeNav="vacancies">
    <div class="container py-4">
        <h1 class="profile-title mb-4"><@spring.message code="vacancies.title"/></h1>
        <script src="/static/Js/vacancies/search.js"></script>
        <script src="/static/Js/vacancies/filters.js"></script>
        <script src="/static/Js/notifications.js"></script>

        <#if successMessage??>
            <div class="alert alert-success mb-4">
                ${successMessage}
            </div>
        </#if>

        <#if errorMessage??>
            <div class="alert alert-danger mb-4">
                ${errorMessage}
            </div>
        </#if>

        <div class="filter-container mb-4">
            <form method="get" action="/vacancies" class="filter-form">
                <input type="hidden" name="page" value="0">
                <input type="hidden" name="size" value="${pageSize}">

                <div class="search-container-wrapper">
                <div class="search-container">
                    <input type="text"
                           id="searchInput"
                           placeholder="<@spring.message code="vacancies.search.placeholder"/>"
                           class="search-input">
                    <svg xmlns="http://www.w3.org/2000/svg"
                         width="24"
                         height="24"
                         viewBox="0 0 24 24"
                         fill="none"
                         stroke="currentColor"
                         stroke-width="2"
                         stroke-linecap="round"
                         stroke-linejoin="round"
                         class="search-icon">
                        <circle cx="11" cy="11" r="8"></circle>
                        <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                    </svg>
                    <div id="searchResults" class="search-results"></div>
                </div>
                </div>

                <div class="filter-group">
                    <label for="category" class="filter-label"><@spring.message code="category.label"/></label>
                    <select id="category" name="categoryId" class="filter-select">
                        <option value=""><@spring.message code="category.all"/></option>
                        <#list categories as category>
                            <option value="${category.id}" <#if selectedCategory?? && selectedCategory == category.id>selected</#if>>
                                ${category.name}
                            </option>
                        </#list>
                    </select>
                </div>

                <div class="filter-group">
                    <label for="region" class="filter-label"><@spring.message code="region.label"/></label>
                    <select id="region" name="regionId" class="filter-select">
                        <option value=""><@spring.message code="region.all"/></option>
                        <#list regions as region>
                            <option value="${region.id}" <#if selectedRegion?? && selectedRegion == region.id>selected</#if>>
                                ${region.name}
                            </option>
                        </#list>
                    </select>
                </div>

                <div class="filter-group">
                    <label for="sort" class="filter-label"><@spring.message code="sort.label"/></label>
                    <select id="sort" name="sort" class="filter-select">
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

                <div class="filter-group" style="flex: 0 0 auto;">
                    <button type="button" id="apply-filters" class="btn filter-btn">
                        <i class="bi bi-funnel"></i>
                        <@spring.message code="filter.apply"/>
                    </button>
                </div>

                <#if selectedSort?? || selectedCategory?? || selectedRegion??>
                    <div class="filter-group" style="flex: 0 0 auto;">
                        <a href="/vacancies?page=0&size=${pageSize}" class="btn filter-btn filter-btn-reset">
                            <i class="bi bi-arrow-counterclockwise"></i>
                            <@spring.message code="filter.reset"/>
                        </a>
                    </div>
                </#if>
            </form>
        </div>

        <#if vacancies?size == 0>
            <div class="card border-0 shadow-sm p-5 text-center">
                <div class="card-body">
                    <h3 class="mb-3 text-primary"><@spring.message code="vacancies.empty.title"/></h3>
                    <p class="text-muted"><@spring.message code="vacancies.empty.message"/></p>
                </div>
            </div>
        </#if>

        <div class="row g-4">
            <#list vacancies as vacancy>
                <div class="col-lg-12">
                    <a href="/vacancies/${vacancy.id}/info" class="text-decoration-none">
                    <div class="card vacancy-card h-100 border-0 shadow-sm">
                        <div class="card-body p-4 position-relative">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <h5 class="card-title text-primary">${vacancy.name}</h5>
                                <span class="badge bg-primary rounded-pill fs-5">${vacancy.salary} с</span>
                            </div>

                            <p class="text-muted mb-3">${vacancy.description}</p>

                            <div class="d-flex flex-wrap gap-3 text-muted mb-3">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-clock-history me-1"></i>
                                    <small><@spring.message code="vacancies.card.exp"/> ${vacancy.expFrom}-${vacancy.expTo}
                                        <@spring.message code="vacancies.card.exp.years"/></small>
                                </div>
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-person me-1"></i>
                                    <small><@spring.message code="vacancies.card.author"/> ${vacancy.authorName!""}</small>
                                </div>
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-calendar me-1"></i>
                                    <small><@spring.message code="vacancies.card.date"/> ${vacancy.updateTime?string("dd.MM.yyyy")}</small>
                                </div>
                            </div>

                            <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                                <div class="position-relative">
                                    <form action="/favorites/vacancies/${vacancy.id}/add" method="post" class="favorite-form position-absolute bottom-0 end-0">
                                        <input type="hidden" name="vacancyId" value="${vacancy.id}">
                                        <#if _csrf??>
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        </#if>
                                        <button type="submit" class="btn btn-link p-0 border-0" title="Добавить в избранное">
                                            <svg width="24" height="24" viewBox="0 0 24 24"
                                                 fill="<#if favoriteVacancyIds?? && favoriteVacancyIds?seq_contains(vacancy.id)>gold<#else>none</#if>"
                                                 stroke="black" stroke-width="2"
                                            stroke-linecap="round" stroke-linejoin="round" class="favorite-icon">
                                            <path d="M5 3v18l7-5 7 5V3z"/>
                                            </svg>
                                        </button>
                                    </form>
                                </div>
                            </#if>

                            <div class="d-flex flex-wrap gap-2 mt-3">
                                <#if Session.SPRING_SECURITY_CONTEXT?? && !Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                                    <#if vacancy.authorId??>
                                        <a href="/profile/viewEmployer/${vacancy.authorId}" class="btn btn-sm btn-primary">
                                            <@spring.message code="vacancies.company.profile"/>
                                        </a>
                                    </#if>
                                    <a href="#modal-${vacancy.id}" class="btn btn-sm btn-primary">
                                        <@spring.message code="vacancies.respond.button"/>
                                    </a>
                                </#if>
                            </div>
                        </div>
                    </div>
                    </a>
                </div>

                <div id="modal-${vacancy.id}" class="modal-vacancy">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title"><@spring.message code="vacancies.response.modal.title"/> "${vacancy.name}"</h5>
                                <a href="#" class="btn-close" aria-label="Close"></a>
                            </div>
                            <div class="modal-body">
                                <form action="/response/respond" method="post">
                                    <input type="hidden" name="vacancyId" value="${vacancy.id}">
                                    <#if _csrf??>
                                        <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                                    </#if>

                                    <div class="mb-3">
                                        <label for="resumeId-${vacancy.id}" class="form-label"><@spring.message code="vacancies.response.select.resume"/></label>
                                        <select id="resumeId-${vacancy.id}" name="resumeId" class="form-select" required>
                                            <#if userResumes?? && userResumes?size gt 0>
                                                <#list userResumes as resume>
                                                    <option value="${resume.id}">${resume.name} (${resume.updateTime?string("dd.MM.yyyy")})</option>
                                                </#list>
                                            <#else>
                                                <option value="" disabled><@spring.message code="vacancies.response.no.resumes"/></option>
                                            </#if>
                                        </select>
                                    </div>

                                    <button type="submit" class="btn btn-primary w-100">
                                        <@spring.message code="vacancies.response.submit.button"/>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </#list>
        </div>

        <#if totalPages gt 1>
            <nav aria-label="Page navigation" class="mt-4">
                <ul class="pagination justify-content-center">
                    <#if currentPage gt 0>
                        <li class="page-item">
                            <a class="page-link" href="/vacancies?page=0&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedRegion??>&regionId=${selectedRegion}</#if><#if selectedSort??>&sort=${selectedSort}</#if>" aria-label="First">
                                <span aria-hidden="true">&laquo;&laquo;</span>
                            </a>
                        </li>
                        <li class="page-item">
                            <a class="page-link" href="/vacancies?page=${currentPage - 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedRegion??>&regionId=${selectedRegion}</#if><#if selectedSort??>&sort=${selectedSort}</#if>" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </#if>

                    <#list 0..totalPages-1 as i>
                        <li class="page-item <#if i == currentPage>active</#if>">
                            <a class="page-link" href="/vacancies?page=${i}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedRegion??>&regionId=${selectedRegion}</#if><#if selectedSort??>&sort=${selectedSort}</#if>">${i + 1}</a>
                        </li>
                    </#list>

                    <#if currentPage lt totalPages - 1>
                        <li class="page-item">
                            <a class="page-link" href="/vacancies?page=${currentPage + 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedRegion??>&regionId=${selectedRegion}</#if><#if selectedSort??>&sort=${selectedSort}</#if>" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                        <li class="page-item">
                            <a class="page-link" href="/vacancies?page=${totalPages - 1}&size=${pageSize}<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedRegion??>&regionId=${selectedRegion}</#if><#if selectedSort??>&sort=${selectedSort}</#if>" aria-label="Last">
                                <span aria-hidden="true">&raquo;&raquo;</span>
                            </a>
                        </li>
                    </#if>
                </ul>

                <div class="d-flex justify-content-center align-items-center mt-3">
                    <span class="me-2"><@spring.message code="pagination"/></span>
                    <select class="form-select form-select-sm" style="width: auto;"
                            onchange="window.location.href='/vacancies?page=0&size='+this.value+'<#if selectedCategory??>&categoryId=${selectedCategory}</#if><#if selectedRegion??>&regionId=${selectedRegion}</#if><#if selectedSort??>&sort=${selectedSort}</#if>'">
                        <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                        <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                        <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                        <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                    </select>
                </div>
            </nav>
        </#if>
    </div>
</@layout.page>

<script>
    const messages = {
        notFound: '<@spring.message code="vacancies.not.found"/>'
    };
</script>