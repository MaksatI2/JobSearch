INSERT INTO users (email, name, password, phone_number, account_type)
VALUES ('employer1@example.com', 'James', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270932',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer2@example.com', 'Michael', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270933',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer3@example.com', 'Robert', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270934',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer4@example.com', 'William', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270935',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer5@example.com', 'David', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270936',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer6@example.com', 'Richard', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270937',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer7@example.com', 'Joseph', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270938',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer8@example.com', 'Thomas', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270939',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer9@example.com', 'Charles', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270940',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer10@example.com', 'Christopher', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270941',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer11@example.com', 'Daniel', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270942',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer12@example.com', 'Paul', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270943',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer13@example.com', 'Mark', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270944',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer14@example.com', 'Donald', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270945',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER')),
       ('employer15@example.com', 'Steven', '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270946',
        (SELECT id FROM authorities WHERE authority = 'EMPLOYER'));


INSERT INTO users (email, name, surname, age, password, phone_number, account_type)
VALUES ('applicant1@example.com', 'John', 'Smith', 25, '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270950',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant2@example.com', 'Emma', 'Johnson', 28, '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270951',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant3@example.com', 'Michael', 'Williams', 32,
        '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270952',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant4@example.com', 'Sophia', 'Brown', 24, '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270953',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant5@example.com', 'James', 'Jones', 30, '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270954',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant6@example.com', 'Olivia', 'Garcia', 27,
        '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270955',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant7@example.com', 'Robert', 'Miller', 35,
        '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270956',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant8@example.com', 'Ava', 'Davis', 29, '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270957',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant9@example.com', 'William', 'Rodriguez', 31,
        '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270958',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant10@example.com', 'Isabella', 'Martinez', 26,
        '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270959',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant11@example.com', 'David', 'Hernandez', 33,
        '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270960',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant12@example.com', 'Mia', 'Lopez', 23, '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.',
        '0700270961',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant13@example.com', 'Joseph', 'Gonzalez', 34,
        '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270962',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant14@example.com', 'Charlotte', 'Wilson', 28,
        '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270963',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT')),
       ('applicant15@example.com', 'Daniel', 'Anderson', 30,
        '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.', '0700270964',
        (SELECT id FROM authorities WHERE authority = 'APPLICANT'));