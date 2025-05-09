<#import "/spring.ftl" as spring>

<#macro page title="" styles="" activeNav="">
    <!DOCTYPE html>
    <html lang="ru">
    <title>JobSearch</title>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${title}</title>

        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="/static/css/header.css">
        <#if styles?has_content>
            <link rel="stylesheet" href="${styles}">
        </#if>

        <link rel="icon" href="/static/images/logo-transparent.png" type="image/x-icon">
    </head>
    <body>
    <header class="header">
        <nav class="navbar">
            <a href="/" class="logo">
                <img src="/static/images/logo-transparent.png" alt="JobSearch" class="logo-img">
            </a>
            <div class="nav-links">
                <a href="/" class="nav-link <#if activeNav == "home">active</#if>"><@spring.message code="nav.home" /></a>
                <a href="/vacancies" class="nav-link <#if activeNav == "vacancies">active</#if>"><@spring.message code="nav.vacancies" /></a>

                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <#if Session.SPRING_SECURITY_CONTEXT.authentication.authorities?seq_contains('EMPLOYER')>
                        <a href="/resumes/allResumes" class="nav-link <#if activeNav == "resumes">active</#if>"><@spring.message code="nav.resumes" /></a>
                        <a href="/favorites/resumes" class="nav-link <#if activeNav == "favorite">active</#if>"><@spring.message code="nav.favorites" /></a>
                    <#else>
                        <a href="/company/allCompanies" class="nav-link <#if activeNav == "companies">active</#if>"><@spring.message code="nav.companies" /></a>
                        <a href="/favorites/vacancies" class="nav-link <#if activeNav == "favorite">active</#if>"><@spring.message code="nav.favorites" /></a>
                    </#if>
                </#if>

                <#if Session.SPRING_SECURITY_CONTEXT??>
                    <a href="/profile" class="nav-link profile-link <#if activeNav == "profile">active</#if>">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
                            <path d="M12 4a4 4 0 0 1 4 4 4 4 0 0 1-4 4 4 4 0 0 1-4-4 4 4 0 0 1 4-4m0 10c4.42 0 8 1.79 8 4v2H4v-2c0-2.21 3.58-4 8-4z"/>
                        </svg>
                    </a>
                    <#if activeNav == "profile" && _csrf??>
                        <form action="/logout" method="post">
                            <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                            <button type="submit" class="logout-btn" id="logout"><@spring.message code="nav.logout" /></button>
                        </form>
                    </#if>
                <#else>
                    <a href="/auth/login" class="nav-link <#if activeNav == "login">active</#if>"><@spring.message code="nav.login" /></a>
                    <a href="/auth/register" class="nav-link <#if activeNav == "register">active</#if>"><@spring.message code="nav.register" /></a>
                </#if>
                <div class="lang-switcher">
                    <a href="?lang=ru">RU</a> | <a href="?lang=en">EN</a>
                </div>
            </div>
        </nav>
    </header>

    <main class="main">
        <div class="container">
            <#nested>
        </div>
    </main>

    <footer class="footer">
        <div class="container">
            <p>© 2025 JobSearch. <@spring.message code="footer.rights" /></p>
        </div>
    </footer>
    </body>
    </html>
</#macro>