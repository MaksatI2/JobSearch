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

INSERT INTO education_info (resume_id, institution_name, program, start_date, end_date, degree)
VALUES
    ((SELECT id FROM resumes WHERE name = 'Junior Developer' AND applicant_id = (SELECT id FROM users WHERE email = 'Mark@example.com')), 'Tech University', 'Computer Science Program', '2016-09-01', '2020-06-01', 'Bachelor of Computer Science'),
    ((SELECT id FROM resumes WHERE name = 'Project Assistant' AND applicant_id = (SELECT id FROM users WHERE email = 'Mark@example.com')), 'Business School', 'Business Administration Program', '2015-09-01', '2019-06-01', 'Bachelor of Business Administration'),
    ((SELECT id FROM resumes WHERE name = 'Frontend Developer' AND applicant_id = (SELECT id FROM users WHERE email = 'Islam@example.com')), 'Creative Arts College', 'Design Program', '2017-09-01', '2021-06-01', 'Bachelor of Design'),
    ((SELECT id FROM resumes WHERE name = 'Backend Developer' AND applicant_id = (SELECT id FROM users WHERE email = 'Islam@example.com')), 'Tech Institute', 'Software Engineering Program', '2016-09-01', '2020-06-01', 'Master of Software Engineering');

INSERT INTO work_experience_info (resume_id, company_name, position, years, responsibilities)
VALUES
    ((SELECT id FROM resumes WHERE name = 'Junior Developer' AND applicant_id = (SELECT id FROM users WHERE email = 'Mark@example.com')), 'TechCorp', 'Intern', 3, 'Assisting in software development tasks and testing applications.'),
    ((SELECT id FROM resumes WHERE name = 'Project Assistant' AND applicant_id = (SELECT id FROM users WHERE email = 'Mark@example.com')), 'ProDev Ltd', 'Assistant Project Manager', 2, 'Supporting project management and client communications.'),
    ((SELECT id FROM resumes WHERE name = 'Frontend Developer' AND applicant_id = (SELECT id FROM users WHERE email = 'Islam@example.com')), 'WebDesign Studio', 'Frontend Developer', 1, 'Developing responsive websites and UI/UX design.'),
    ((SELECT id FROM resumes WHERE name = 'Backend Developer' AND applicant_id = (SELECT id FROM users WHERE email = 'Islam@example.com')), 'CodeWorks', 'Backend Developer', 2, 'Developing backend logic for web applications and managing databases.');

