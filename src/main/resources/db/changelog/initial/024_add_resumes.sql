INSERT INTO resumes (applicant_id, category_id, name, salary, is_active)
VALUES
((SELECT id FROM users WHERE email = 'applicant1@example.com'),
 (SELECT id FROM categories WHERE name = 'Preventive Care'),
 'Preventive Health Specialist', 45000, TRUE),

((SELECT id FROM users WHERE email = 'applicant1@example.com'),
 (SELECT id FROM categories WHERE name = 'Medical Treatments'),
 'Clinical Assistant', 40000, FALSE),

((SELECT id FROM users WHERE email = 'applicant2@example.com'),
 (SELECT id FROM categories WHERE name = 'Mental Wellbeing'),
 'Counseling Psychologist', 55000, TRUE),

((SELECT id FROM users WHERE email = 'applicant3@example.com'),
 (SELECT id FROM categories WHERE name = 'Fitness'),
 'Certified Personal Trainer', 38000, TRUE),

((SELECT id FROM users WHERE email = 'applicant3@example.com'),
 (SELECT id FROM categories WHERE name = 'Exercise Programs'),
 'Group Fitness Instructor', 35000, TRUE),

((SELECT id FROM users WHERE email = 'applicant4@example.com'),
 (SELECT id FROM categories WHERE name = 'Nutrition'),
 'Nutrition Consultant', 42000, TRUE),

((SELECT id FROM users WHERE email = 'applicant5@example.com'),
 (SELECT id FROM categories WHERE name = 'Physical Health'),
 'Licensed Practical Nurse', 48000, TRUE),

((SELECT id FROM users WHERE email = 'applicant6@example.com'),
 (SELECT id FROM categories WHERE name = 'Mindfulness Practices'),
 'Meditation Guide', 32000, TRUE),

((SELECT id FROM users WHERE email = 'applicant7@example.com'),
 (SELECT id FROM categories WHERE name = 'Medical Treatments'),
 'Physical Therapy Aide', 36000, TRUE),

((SELECT id FROM users WHERE email = 'applicant8@example.com'),
 (SELECT id FROM categories WHERE name = 'Healthy Diets'),
 'Dietetic Technician', 40000, TRUE),

((SELECT id FROM users WHERE email = 'applicant9@example.com'),
 (SELECT id FROM categories WHERE name = 'Stress Management'),
 'Wellness Coach', 47000, TRUE),

((SELECT id FROM users WHERE email = 'applicant10@example.com'),
 (SELECT id FROM categories WHERE name = 'Fitness'),
 'Yoga Instructor', 37000, TRUE),

((SELECT id FROM users WHERE email = 'applicant11@example.com'),
 (SELECT id FROM categories WHERE name = 'Preventive Care'),
 'Health Educator', 43000, TRUE),

((SELECT id FROM users WHERE email = 'applicant12@example.com'),
 (SELECT id FROM categories WHERE name = 'Nutrition'),
 'Nutritional Assistant', 34000, FALSE),

((SELECT id FROM users WHERE email = 'applicant13@example.com'),
 (SELECT id FROM categories WHERE name = 'Medical Treatments'),
 'Medical Massage Therapist', 52000, TRUE),

((SELECT id FROM users WHERE email = 'applicant14@example.com'),
 (SELECT id FROM categories WHERE name = 'Mental Wellbeing'),
 'Art Therapist', 39000, TRUE),

((SELECT id FROM users WHERE email = 'applicant15@example.com'),
 (SELECT id FROM categories WHERE name = 'Exercise Programs'),
 'Senior Fitness Specialist', 50000, TRUE);

INSERT INTO education_info (resume_id, institution_name, program, start_date, end_date, degree)
VALUES
((SELECT id FROM resumes WHERE name = 'Preventive Health Specialist'), 'State Medical University', 'Public Health',
 '2015-09-01', '2019-06-01', 'Bachelor of Science'),
((SELECT id FROM resumes WHERE name = 'Clinical Assistant'), 'Community College', 'Clinical Medical Assistant',
 '2019-09-01', '2020-06-01', 'Certificate'),

((SELECT id FROM resumes WHERE name = 'Counseling Psychologist'), 'Psychology Institute', 'Clinical Psychology',
 '2014-09-01', '2018-06-01', 'Master of Arts'),

((SELECT id FROM resumes WHERE name = 'Certified Personal Trainer'), 'National Fitness Academy', 'Exercise Science',
 '2017-09-01', '2019-06-01', 'Associate Degree'),

((SELECT id FROM resumes WHERE name = 'Nutrition Consultant'), 'Nutrition College', 'Dietetics', '2016-09-01',
 '2020-06-01', 'Bachelor of Science'),

((SELECT id FROM resumes WHERE name = 'Licensed Practical Nurse'), 'Medical Training Institute', 'Practical Nursing',
 '2018-01-01', '2019-06-01', 'Diploma'),

((SELECT id FROM resumes WHERE name = 'Meditation Guide'), 'Mindfulness Center', 'Meditation Instruction', '2020-01-01',
 '2020-06-01', 'Certificate'),

((SELECT id FROM resumes WHERE name = 'Physical Therapy Aide'), 'Health Sciences College', 'Physical Therapy',
 '2017-09-01', '2021-06-01', 'Bachelor of Science'),

((SELECT id FROM resumes WHERE name = 'Dietetic Technician'), 'Nutrition Institute', 'Food and Nutrition', '2015-09-01',
 '2019-06-01', 'Bachelor of Science'),

((SELECT id FROM resumes WHERE name = 'Wellness Coach'), 'Wellness University', 'Health Coaching', '2016-09-01',
 '2020-06-01', 'Bachelor of Science');

INSERT INTO work_experience_info (resume_id, company_name, position, years, responsibilities)
VALUES
((SELECT id FROM resumes WHERE name = 'Preventive Health Specialist'), 'Community Health Center', 'Health Screener', 2,
 'Conducted health screenings and preventive care education'),
((SELECT id FROM resumes WHERE name = 'Clinical Assistant'), 'City General Hospital', 'Medical Assistant', 1,
 'Assisted physicians with patient care and documentation'),

((SELECT id FROM resumes WHERE name = 'Counseling Psychologist'), 'Mental Health Clinic', 'Counselor', 3,
 'Provided individual and group counseling sessions'),

((SELECT id FROM resumes WHERE name = 'Certified Personal Trainer'), 'Elite Fitness Club', 'Trainer', 2,
 'Developed personalized fitness programs for clients'),
((SELECT id FROM resumes WHERE name = 'Group Fitness Instructor'), 'City Sports Center', 'Instructor', 1,
 'Led group exercise classes'),

((SELECT id FROM resumes WHERE name = 'Nutrition Consultant'), 'Healthy Living Center', 'Nutrition Advisor', 1,
 'Provided dietary consultations'),

((SELECT id FROM resumes WHERE name = 'Licensed Practical Nurse'), 'Long-Term Care Facility', 'LPN', 3,
 'Provided nursing care to residents'),

((SELECT id FROM resumes WHERE name = 'Meditation Guide'), 'Peaceful Mind Studio', 'Meditation Teacher', 2,
 'Conducted meditation sessions'),

((SELECT id FROM resumes WHERE name = 'Physical Therapy Aide'), 'Rehabilitation Center', 'Therapy Assistant', 1,
 'Assisted physical therapists with patient care'),

((SELECT id FROM resumes WHERE name = 'Dietetic Technician'), 'Nutrition Clinic', 'Dietary Assistant', 2,
 'Supported dietitians with meal planning'),

((SELECT id FROM resumes WHERE name = 'Wellness Coach'), 'Corporate Wellness Program', 'Coach', 2,
 'Delivered stress management workshops');

INSERT INTO contact_info (resume_id, type_id, text)
VALUES
((SELECT id FROM resumes WHERE name = 'Preventive Health Specialist'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '+77001234567'),

((SELECT id FROM resumes WHERE name = 'Preventive Health Specialist'),
 (SELECT id FROM contact_types WHERE name = 'Email'), 'john.smith@example.com'),

((SELECT id FROM resumes WHERE name = 'Counseling Psychologist'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '+77011234568'),

((SELECT id FROM resumes WHERE name = 'Certified Personal Trainer'),
 (SELECT id FROM contact_types WHERE name = 'Website'), 'fitwithmichael.com'),

((SELECT id FROM resumes WHERE name = 'Nutrition Consultant'),
 (SELECT id FROM contact_types WHERE name = 'Telegram'), '@sophiab_nutrition'),

((SELECT id FROM resumes WHERE name = 'Licensed Practical Nurse'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '+77041234571'),

((SELECT id FROM resumes WHERE name = 'Meditation Guide'),
 (SELECT id FROM contact_types WHERE name = 'Email'), 'olivia.garcia@example.com'),

((SELECT id FROM resumes WHERE name = 'Physical Therapy Aide'),
 (SELECT id FROM contact_types WHERE name = 'LinkedIn'), 'linkedin.com/in/robertmiller-therapy'),

((SELECT id FROM resumes WHERE name = 'Dietetic Technician'),
 (SELECT id FROM contact_types WHERE name = 'Email'), 'ava.davis@example.com'),

((SELECT id FROM resumes WHERE name = 'Wellness Coach'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '+77061234573'),

((SELECT id FROM resumes WHERE name = 'Health Educator'),
 (SELECT id FROM contact_types WHERE name = 'Website'), 'healthwithdavid.com'),

((SELECT id FROM resumes WHERE name = 'Nutritional Assistant'),
 (SELECT id FROM contact_types WHERE name = 'Telegram'), '@mialopez_nutrition'),

((SELECT id FROM resumes WHERE name = 'Medical Massage Therapist'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '+77071234574'),

((SELECT id FROM resumes WHERE name = 'Art Therapist'),
 (SELECT id FROM contact_types WHERE name = 'Email'), 'charlotte.wilson@example.com'),

((SELECT id FROM resumes WHERE name = 'Senior Fitness Specialist'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '+77081234575');