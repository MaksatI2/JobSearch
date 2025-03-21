CREATE TABLE IF NOT EXISTS categories
(
    id        LONG AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255),
    parent_id LONG,
    FOREIGN KEY (parent_id) REFERENCES categories (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id           LONG AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    name         VARCHAR(255) NOT NULL,
    surname      VARCHAR(255) NOT NULL,
    age          INT,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    avatar       VARCHAR(255),
    account_type VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS resumes
(
    id           LONG AUTO_INCREMENT PRIMARY KEY,
    applicant_id LONG,
    category_id  LONG,
    name         VARCHAR(255),
    salary       FLOAT,
    create_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active    BOOLEAN   DEFAULT TRUE,
    update_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (applicant_id) REFERENCES users (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS contact_types
(
    id   LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS contact_info
(
    id        LONG AUTO_INCREMENT PRIMARY KEY,
    resume_id LONG,
    type_id   LONG,
    Text      VARCHAR(255),
    FOREIGN KEY (resume_id) REFERENCES resumes (id),
    FOREIGN KEY (type_id) REFERENCES contact_types (id)
);

CREATE TABLE IF NOT EXISTS education_info
(
    id          LONG AUTO_INCREMENT PRIMARY KEY,
    resume_id   LONG,
    institution VARCHAR(255),
    program     VARCHAR(255),
    start_date  DATE,
    end_date    DATE,
    degree      VARCHAR(255),
    FOREIGN KEY (resume_id) REFERENCES resumes (id)
);

CREATE TABLE IF NOT EXISTS vacancies
(
    id           LONG AUTO_INCREMENT PRIMARY KEY,
    author_id    LONG,
    category_id  LONG,
    name         VARCHAR(255),
    description  TEXT,
    salary       FLOAT,
    exp_from     INT,
    exp_to       INT,
    is_active    BOOLEAN   DEFAULT TRUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS responded_applicants
(
    id           LONG AUTO_INCREMENT PRIMARY KEY,
    resume_id    LONG,
    vacancy_id   LONG,
    confirmation BOOLEAN,
    FOREIGN KEY (resume_id) REFERENCES resumes (id),
    FOREIGN KEY (vacancy_id) REFERENCES vacancies (id)
);

CREATE TABLE IF NOT EXISTS messages
(
    id                   LONG AUTO_INCREMENT PRIMARY KEY,
    responded_applicants LONG,
    description          VARCHAR(255),
    send_time            TIMESTAMP,
    FOREIGN KEY (responded_applicants) REFERENCES responded_applicants (id)
);

CREATE TABLE IF NOT EXISTS work_experience_info
(
    id               LONG AUTO_INCREMENT PRIMARY KEY,
    resume_id        LONG,
    year_work        LONG,
    company_name     VARCHAR(255),
    position         VARCHAR(255),
    responsibilities VARCHAR(255),
    FOREIGN KEY (resume_id) REFERENCES resumes (id)
);

INSERT INTO categories (name, parent_id)
VALUES ('Software development', NULL),
       ('Project management', NULL),
       ('Backend development', 1),
       ('Frontend development', 1);

INSERT INTO users (email, name, surname, age, password, phone_number, account_type)
VALUES ('Mark@example.com', 'Maks', 'Das', 26, 'password1', '0700270932', 'APPLICANT'),
       ('Islam@example.com', 'Islam', 'Osmonov', 19, 'password2', '0700270932', 'APPLICANT'),
       ('Ricki@example.com', 'Ricki', 'Gosling', 35, 'password3', '0500345464', 'EMPLOYER'),
       ('Jane@example.com', 'Jane', 'Smith', 40, 'password4', '0990122121', 'EMPLOYER'),
       ('Jane2@example.com', 'Jane', 'West', 28, 'password5', '0990122121', 'EMPLOYER');

INSERT INTO vacancies (author_id, category_id, name, description, salary, exp_from, exp_to, is_active)
VALUES ((SELECT id FROM users WHERE email = 'Ricki@example.com'), (SELECT id FROM categories WHERE name = 'Software development'), 'Software Developer', 'Develop software applications', 50000, 1, 3, TRUE),
       ((SELECT id FROM users WHERE email = 'Ricki@example.com'), (SELECT id FROM categories WHERE name = 'Project management'), 'Project Manager', 'Manage software projects', 65000, 2, 4, TRUE),
       ((SELECT id FROM users WHERE email = 'Islam@example.com'), (SELECT id FROM categories WHERE name = 'Frontend development'), 'Backend Junior Developer', 'Mobile game development', 90000, 5, 6, TRUE),
       ((SELECT id FROM users WHERE email = 'Islam@example.com'), (SELECT id FROM categories WHERE name = 'Backend development'), 'Frontend Developer', 'Website development', 55000, 1, 3, FALSE);

INSERT INTO resumes (applicant_id, category_id, name, salary, is_active)
VALUES ((SELECT id FROM users WHERE email = 'Mark@example.com'), (SELECT id FROM categories WHERE name = 'Software development'), 'Junior Developer', 40000, TRUE),
       ((SELECT id FROM users WHERE email = 'Mark@example.com'), (SELECT id FROM categories WHERE name = 'Project management'), 'Project Assistant', 45000, TRUE),
       ((SELECT id FROM users WHERE email = 'Islam@example.com'), (SELECT id FROM categories WHERE name = 'Frontend development'), 'Frontend Developer', 85000, FALSE),
       ((SELECT id FROM users WHERE email = 'Islam@example.com'), (SELECT id FROM categories WHERE name = 'Backend development'), 'Backend Developer', 60000, TRUE);

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES ((SELECT id FROM resumes WHERE name = 'Junior Developer' AND applicant_id = (SELECT id FROM users WHERE email = 'Mark@example.com')),
        (SELECT id FROM vacancies WHERE name = 'Software Developer'), TRUE),
       ((SELECT id FROM resumes WHERE name = 'Junior Developer' AND applicant_id = (SELECT id FROM users WHERE email = 'Mark@example.com')),
        (SELECT id FROM vacancies WHERE name = 'Project Manager'), FALSE),
       ((SELECT id FROM resumes WHERE name = 'Frontend Developer' AND applicant_id = (SELECT id FROM users WHERE email = 'Islam@example.com')),
        (SELECT id FROM vacancies WHERE name = 'Project Manager'), FALSE),
       ((SELECT id FROM resumes WHERE name = 'Frontend Developer' AND applicant_id = (SELECT id FROM users WHERE email = 'Islam@example.com')),
        (SELECT id FROM vacancies WHERE name = 'Frontend Developer'), FALSE);