<#import "../layout.ftlh" as layout>
<#import "/spring.ftl" as spring>

<@layout.page styles="/static/css/companies/allCompanies.css" activeNav="companies">
    <div class="container py-5">
        <h1 class="mb-4 position-relative pb-3"><@spring.message code="companies.title"/></h1>

        <#if companies?? && companies?size gt 0>
            <div class="row g-4 mt-4">
                <#list companies as company>
                    <div class="col-12">
                        <a href="/profile/viewEmployer/${company.id}" class="text-decoration-none text-dark">
                            <div class="card border-0 shadow-sm rounded-3 p-4 h-100 hover-lift">
                                <div class="d-flex align-items-center mb-4">
                                    <img src="${company.avatar}" alt="Company Logo" class="rounded-circle me-4" width="70" height="70" style="object-fit: cover; border: 2px solid #E0F2F0;">
                                    <h3 class="h4 mb-0">${company.name}</h3>
                                </div>
                                <div class="text-secondary">
                                    <div class="d-flex align-items-center mb-3">
                                        <span class="me-2">✉️</span>
                                        <span>${company.email}</span>
                                    </div>
                                    <div class="d-flex align-items-center">
                                        <span class="me-2">📞</span>
                                        <span>${company.phoneNumber}</span>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </div>
                </#list>
            </div>

            <#if totalPages gt 1>
                <div class="d-flex flex-wrap justify-content-center align-items-center mt-5 gap-2">
                    <#if currentPage gt 0>
                        <a href="/company/allCompanies?page=0&size=${pageSize}" class="btn btn-outline-primary rounded">««</a>
                        <a href="/company/allCompanies?page=${currentPage - 1}&size=${pageSize}" class="btn btn-outline-primary rounded">«</a>
                    </#if>

                    <#list 0..totalPages-1 as i>
                        <#if i == currentPage>
                            <a href="/company/allCompanies?page=${i}&size=${pageSize}" class="btn btn-primary rounded">${i + 1}</a>
                        <#else>
                            <a href="/company/allCompanies?page=${i}&size=${pageSize}" class="btn btn-outline-primary rounded">${i + 1}</a>
                        </#if>
                    </#list>

                    <#if currentPage lt totalPages - 1>
                        <a href="/company/allCompanies?page=${currentPage + 1}&size=${pageSize}" class="btn btn-outline-primary rounded">»</a>
                        <a href="/company/allCompanies?page=${totalPages - 1}&size=${pageSize}" class="btn btn-outline-primary rounded">»»</a>
                    </#if>

                    <div class="d-flex align-items-center ms-4 gap-2">
                        <@spring.message code="pagination"/>
                        <select onchange="window.location.href='/company/allCompanies?page=0&size='+this.value" class="form-select form-select-sm" style="width: auto;">
                            <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                            <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                            <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                            <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                        </select>
                    </div>
                </div>
            </#if>
        <#else>
            <div class="card border-0 shadow-sm rounded-3 text-center py-5 px-3">
                <p class="text-secondary mb-0"><@spring.message code="companies.error"/></p>
            </div>
        </#if>
    </div>
</@layout.page>