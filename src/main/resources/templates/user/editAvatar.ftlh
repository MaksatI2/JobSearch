<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.page styles="/static/css/user/editAvatar.css" activeNav="profile">
    <section class="container py-4">
        <div class="edit-avatar-section bg-white p-4 p-md-5 rounded shadow">
            <h2 class="mb-4"><@spring.message code="profile.edit.avatar.title"/></h2>

            <div class="row">
                <div class="col-md-6 mb-4 mb-md-0">
                    <div class="current-avatar-container p-3 rounded">
                        <h3 class="mb-3"><@spring.message code="profile.edit.avatar.view.avatar"/></h3>
                        <div class="avatar-wrapper">
                            <img src="/api/users/${user.id}/avatar" alt="Аватар пользователя"
                                 class="current-avatar img-thumbnail rounded-circle"
                                 onerror="this.src='/static/default-avatar/default_avatar.jpg'">
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="upload-container p-3 rounded">
                        <h3 class="mb-3"><@spring.message code="profile.edit.avatar.new"/></h3>
                        <form action="/profile/avatar" method="post" enctype="multipart/form-data" class="avatar-form" id="avatarForm">
                            <#if _csrf??>
                                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                                       value="${(_csrf.token)!'csrf-token'}"/>
                            </#if>
                            <input type="hidden" name="userId" value="${user.id}">

                            <div class="file-input-wrapper mb-3">
                                <label class="file-label btn btn-primary w-100">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                         stroke-linejoin="round" class="upload-icon me-2">
                                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                                        <polyline points="17 8 12 3 7 8"></polyline>
                                        <line x1="12" y1="3" x2="12" y2="15"></line>
                                    </svg>
                                    <@spring.message code="profile.edit.avatar.choose"/>
                                    <input type="file" name="file" accept="image/*"
                                           class="file-input d-none" id="avatarFileInput">
                                </label>
                                <div id="fileError" class="text-danger mt-2" style="display: none;"></div>
                            </div>

                            <button type="submit" class="btn-save-avatar btn btn-success w-100">
                                <@spring.message code="profile.edit.avatar.save"/>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const MAX_FILE_SIZE = 5 * 1024 * 1024;
            const form = document.getElementById('avatarForm');
            const fileInput = document.getElementById('avatarFileInput');
            const fileError = document.getElementById('fileError');

            form.addEventListener('submit', function(e) {
                fileError.style.display = 'none';

                if (fileInput.files.length > 0) {
                    const file = fileInput.files[0];

                    if (file.size > MAX_FILE_SIZE) {
                        e.preventDefault();
                        fileError.textContent = '<@spring.message code="profile.edit.avatar.error.size" arguments=["5"]/>';
                        fileError.style.display = 'block';
                    }
                }
            });

            fileInput.addEventListener('change', function() {
                if (this.files.length > 0) {
                    const file = this.files[0];

                    if (file.size > MAX_FILE_SIZE) {
                        fileError.textContent = '<@spring.message code="profile.edit.avatar.error.size" arguments=["5"]/>';
                        fileError.style.display = 'block';
                        this.value = '';
                    } else {
                        fileError.style.display = 'none';
                    }
                }
            });
        });
    </script>
</@layout.page>