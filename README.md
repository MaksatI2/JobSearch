# JobSearch - Платформа для поиска работы

JobSearch - это современная веб-платформа, которая соединяет работодателей и соискателей, предоставляя удобные инструменты для поиска работы и талантов.

## 🚀 Основные функции

### Для соискателей:
- **Создание и управление резюме** - подробные профили с опытом работы и навыками
- **Поиск вакансий** - удобный поиск по различным критериям
- **Просмотр компаний** - информация о потенциальных работодателях
- **Система чатов** - прямое общение с работодателями
- **Персональный профиль** - управление личной информацией
- **Аналитика** - отслеживание откликов и активности

### Для работодателей:
- **Размещение вакансий** - создание и управление объявлениями о работе
- **Поиск кандидатов** - просмотр резюме потенциальных сотрудников
- **Управление компанией** - корпоративный профиль
- **Система чатов** - общение с кандидатами
- **Аналитика** - отслеживание откликов и активности

## 🛠 Технологический стек

- **Backend**: Java Spring Boot
- **Frontend**: FreeMarker Templates + Bootstrap CSS
- **База данных**: H2
- **Безопасность**: Spring Security
- **Интернационализация**: Spring i18n (русский, английский, греческий)
- **UI компоненты**: Font Awesome Icons

## 📋 Требования

- Java 17 или выше
- Maven 3.6+

## ⚙️ Установка и запуск

### 1. Клонирование репозитория
```bash
git clone https://github.com/your-username/jobsearch.git
cd jobsearch
```

### 2. Настройка базы данных

Создайте базу данных и обновите настройки в `application.properties`:

```properties
spring.datasource.url: jdbc:h2:./db/job-search;AUTO_SERVER=TRUE
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Сборка и запуск
```bash
mvn clean install
mvn spring-boot:run
```

Приложение будет доступно по адресу: `http://localhost:9099`

## 🗄️ Структура базы данных

### Основные таблицы:
- `users` - пользователи системы
- `resumes` - резюме соискателей
- `vacancies` - вакансии от работодателей
- `categories` - категории вакансий/резюме
- `messages` - система чатов
- `responses` - отклики на вакансии

## 🎨 Интерфейс

## 🌐 Интернационализация

Поддерживаемые языки:
- 🇷🇺 Русский (по умолчанию)
- 🇬🇧 English
- 🇬🇷 Ελληνικά

Переключение языка доступно в верхней части страницы.

## 🔐 Безопасность

- **Аутентификация**: Spring Security с формой входа
- **Авторизация**: Role-based access control (RBAC)
- **CSRF защита**: включена для всех форм
- **Валидация данных**: серверная и клиентская валидация
- **Безопасные пароли**: хеширование с использованием BCrypt

### Роли пользователей:
- `ROLE_APPLICANT` - соискатели
- `ROLE_EMPLOYER` - работодатели

## 📱 API Endpoints

### Публичные страницы
- `GET /` - главная страница
- `GET /auth/login` - страница входа
- `GET /auth/register` - страница регистрации
- `POST /auth/register/applicant` - регистрация соискателя
- `POST /auth/register/employer` - регистрация работодателя
- `GET /auth/forgot_password` - форма восстановления пароля
- `POST /auth/forgot_password` - обработка запроса на восстановление пароля
- `GET /auth/reset_password` - форма сброса пароля
- `POST /auth/reset_password` - обработка сброса пароля

### OAuth2 авторизация
- `GET /auth/oauth2/account-type` - выбор типа аккаунта после OAuth2
- `POST /auth/oauth2/account-type` - установка типа аккаунта
- `GET /auth/oauth2/complete-profile` - завершение профиля после OAuth2
- `POST /auth/oauth2/complete-profile` - сохранение дополнительных данных профиля

### Вакансии
- `GET /vacancies` - список вакансий с фильтрацией и пагинацией
- `GET /vacancies/{id}/info` - детальная информация о вакансии
- `GET /vacancies/search` - поиск вакансий (AJAX)
- `GET /vacancies/{userId}/response` - вакансии с откликами для работодателя

#### Управление вакансиями (для работодателей)
- `GET /vacancies/create` - форма создания вакансии
- `POST /vacancies/create` - создание новой вакансии
- `GET /vacancies/{id}/edit` - форма редактирования вакансии
- `POST /vacancies/{id}/edit` - обновление вакансии
- `POST /vacancies/{id}/refresh` - обновление даты публикации вакансии
- `POST /vacancies/{id}/delete` - удаление вакансии

### Резюме
- `GET /resumes/allResumes` - список всех активных резюме с пагинацией
- `GET /resumes/{id}/info` - детальная информация о резюме
- `GET /resumes/{userId}/response` - резюме с откликами для соискателя

#### Управление резюме (для соискателей)
- `GET /resumes/create` - форма создания резюме
- `POST /resumes/create` - создание нового резюме
- `GET /resumes/{id}/edit` - форма редактирования резюме
- `POST /resumes/{id}/edit` - обновление резюме
- `POST /resumes/{id}/refresh` - обновление даты публикации резюме
- `POST /resumes/{id}/delete` - удаление резюме

### Система откликов
- `GET /response/vacancies/{id}` - отклики на вакансию (для работодателей)
- `POST /response/respond` - отклик на вакансию (для соискателей)
- `GET /response/resumes/{id}` - отклики по резюме (для соискателей)

### Система сообщений
- `GET /messages` - список чатов с пагинацией
- `GET /messages/{respondedApplicantId}` - конкретный чат между участниками
- `GET /messages/unread-count` - количество непрочитанных сообщений (AJAX)

### WebSocket для чатов
- `WEBSOCKET /chat.send` - отправка сообщения в реальном времени
- `TOPIC /topic/chat.{respondedApplicantId}` - подписка на обновления чата
- `USER /queue/messages` - личные уведомления о новых сообщениях

## 📦 Развертывание

### Docker :
```dockerfile
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9099
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 🤝 Вклад в проект

1. Fork репозитория
2. Создайте feature branch (`git checkout -b feature/amazing-feature`)
3. Commit изменения (`git commit -m 'Add amazing feature'`)
4. Push в branch (`git push origin feature/amazing-feature`)
5. Создайте Pull Request


## 📞 Контакты

- **Email**: zer0icemax@gmail.com
- **Телефон**: +996 700 27 09 84

## 🔄 Changelog

### v1.0.0 (Текущая версия)
- ✅ Базовая функциональность для соискателей и работодателей
- ✅ Система регистрации и аутентификации
- ✅ Мультиязычность
- ✅ Система чатов
- ✅ Поиск и фильтрация

### Планируемые обновления:
- 📧 Push уведомления
- 📊 Расширенная база
- 🔍 Улучшенный поиск с AI
- 📱 Мобильное приложение
