<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page title="Редактирование аватарки профиля" styles="/static/css/user/editAvatar.css" activeNav="profile">
    <section class="edit-avatar-section">
        <h2>Сменить аватар</h2>

        <div class="current-avatar-container">
            <h3>Текущий аватар</h3>
            <div class="avatar-wrapper">
                <img src="/api/users/${user.id}/avatar" alt="Аватар пользователя" class="current-avatar" onerror="this.src='/static/default-avatar/default_avatar.jpg'">
            </div>
        </div>

        <div class="upload-container">
            <h3>Загрузить новый аватар</h3>
            <form action="/profile/avatar" method="post" enctype="multipart/form-data" class="avatar-form">
                <#if _csrf??>
                    <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}" value="${(_csrf.token)!'csrf-token'}"/>
                </#if>
                <input type="hidden" name="userId" value="${user.id}">
                <div class="file-input-wrapper">
                    <span class="file-label">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="upload-icon">
                            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                            <polyline points="17 8 12 3 7 8"></polyline>
                            <line x1="12" y1="3" x2="12" y2="15"></line>
                        </svg>
                        Выберите файл изображения
                    </span>
                    <input type="file" name="file" accept="image/*" required class="file-input">
                </div>
                <button type="submit" class="btn-save-avatar">Сохранить аватар</button>
            </form>
        </div>
        <div class="avatar-requirements">
            <h4>Требования к изображению:</h4>
            <ul>
                <li>Максимальный размер файла: 5МБ</li>
                <li>Поддерживаемые форматы: JPG,JPEG</li>
                <li>Рекомендуемый размер: 300x300 пикселей</li>
            </ul>
        </div>
    </section>
</@layout.page>