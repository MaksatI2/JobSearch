<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/companies/allCompanies.css" activeNav="companies">
    <h1>Список компаний</h1>

    <#if companies?? && companies?size gt 0>
        <div class="company-list">
            <#list companies as company>
                <a href="/profile/viewEmployer/${company.id}" class="company-card-link">
                    <div class="company-card">
                        <div class="company-header">
                            <img src="${company.avatar}" alt="Company Logo" class="company-avatar">
                            <h3 class="company-name">${company.name}</h3>
                        </div>
                        <div class="company-contacts">
                            <div class="contact-item">
                                <span>✉️</span>
                                <span>${company.email}</span>
                            </div>
                            <div class="contact-item">
                                <span>📞</span>
                                <span>${company.phoneNumber}</span>
                            </div>
                        </div>
                    </div>
                </a>
            </#list>
        </div>
        <#if totalPages gt 1>
            <div class="pagination">
                <#if currentPage gt 0>
                    <a href="/company/allCompanies?page=0&size=${pageSize}" class="page-link">««</a>
                    <a href="/company/allCompanies?page=${currentPage - 1}&size=${pageSize}" class="page-link">«</a>
                </#if>
                <#list 0..totalPages-1 as i>
                    <#if i == currentPage>
                        <span class="page-link active">${i + 1}</span>
                    <#else>
                        <a href="/company/allCompanies?page=${i}&size=${pageSize}" class="page-link">${i + 1}</a>
                    </#if>
                </#list>
                <#if currentPage lt totalPages - 1>
                    <a href="/company/allCompanies?page=${currentPage + 1}&size=${pageSize}" class="page-link">»</a>
                    <a href="/company/allCompanies?page=${totalPages - 1}&size=${pageSize}" class="page-link">»»</a>
                </#if>
                <div class="page-size">
                    Элементов на странице:
                    <select onchange="window.location.href='/company/allCompanies?page=0&size='+this.value" class="page-size-select">
                        <option value="5" <#if pageSize == 5>selected</#if>>5</option>
                        <option value="10" <#if pageSize == 10>selected</#if>>10</option>
                        <option value="20" <#if pageSize == 20>selected</#if>>20</option>
                        <option value="50" <#if pageSize == 50>selected</#if>>50</option>
                    </select>
                </div>
            </div>
        </#if>
    <#else>
        <div class="no-companies">
            <p>На данный момент нет зарегистрированных компаний</p>
        </div>
    </#if>
</@layout.page>