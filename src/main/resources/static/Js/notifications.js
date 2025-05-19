document.addEventListener('DOMContentLoaded', function() {
    // Находим существующие сообщения
    const successMessage = document.querySelector('.alert-success');
    const errorMessage = document.querySelector('.alert-danger');

    // Функция для создания и показа уведомления
    function showNotification(message, type) {
        if (!message) return;

        // Получаем текст сообщения
        const messageText = message.textContent.trim();

        // Удаляем стандартное сообщение
        message.remove();

        // Создаем уведомление
        const notification = document.createElement('div');
        notification.className = `notification ${type === 'success' ? 'notification-success' : 'notification-error'}`;
        notification.innerHTML = `
            <div class="notification-content">
                <span>${messageText}</span>
                <button class="notification-close">✕</button>
            </div>
        `;

        // Стили для уведомления
        notification.style.position = 'fixed';
        notification.style.top = '20px';
        notification.style.right = '20px';
        notification.style.minWidth = '300px';
        notification.style.padding = '15px';
        notification.style.borderRadius = '4px';
        notification.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.2)';
        notification.style.animation = 'slideIn 0.5s ease';
        notification.style.zIndex = '9999';
        notification.style.display = 'flex';
        notification.style.alignItems = 'center';
        notification.style.justifyContent = 'space-between';

        if (type === 'success') {
            notification.style.backgroundColor = '#d4edda';
            notification.style.color = '#155724';
            notification.style.border = '1px solid #c3e6cb';
        } else {
            notification.style.backgroundColor = '#f8d7da';
            notification.style.color = '#721c24';
            notification.style.border = '1px solid #f5c6cb';
        }

        // Стили для содержимого уведомления
        notification.querySelector('.notification-content').style.display = 'flex';
        notification.querySelector('.notification-content').style.justifyContent = 'space-between';
        notification.querySelector('.notification-content').style.alignItems = 'center';
        notification.querySelector('.notification-content').style.width = '100%';

        // Стили для кнопки закрытия
        const closeButton = notification.querySelector('.notification-close');
        closeButton.style.background = 'none';
        closeButton.style.border = 'none';
        closeButton.style.cursor = 'pointer';
        closeButton.style.fontSize = '16px';
        closeButton.style.marginLeft = '10px';
        closeButton.style.padding = '0 5px';

        if (type === 'success') {
            closeButton.style.color = '#155724';
        } else {
            closeButton.style.color = '#721c24';
        }

        // Добавляем на страницу
        document.body.appendChild(notification);

        // Обработчик для кнопки закрытия
        closeButton.addEventListener('click', function() {
            notification.style.animation = 'slideOut 0.5s ease';
            setTimeout(() => {
                notification.remove();
            }, 500);
        });

        // Автоматическое закрытие через 5 секунд
        setTimeout(() => {
            if (document.body.contains(notification)) {
                notification.style.animation = 'slideOut 0.5s ease';
                setTimeout(() => {
                    if (document.body.contains(notification)) {
                        notification.remove();
                    }
                }, 500);
            }
        }, 5000);
    }

    // Добавляем CSS анимацию
    const styleElement = document.createElement('style');
    styleElement.textContent = `
        @keyframes slideIn {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        
        @keyframes slideOut {
            from { transform: translateX(0); opacity: 1; }
            to { transform: translateX(100%); opacity: 0; }
        }
    `;
    document.head.appendChild(styleElement);

    // Показываем уведомления, если они есть на странице
    if (successMessage) {
        showNotification(successMessage, 'success');
    }

    if (errorMessage) {
        showNotification(errorMessage, 'error');
    }
});