
DELETE FROM contact_info;
ALTER TABLE contact_info ALTER COLUMN id RESTART WITH 1;

INSERT INTO contact_info (resume_id, type_id, text) VALUES
((SELECT id FROM resumes WHERE name = 'Preventive Health Specialist'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '996700123456'),

((SELECT id FROM resumes WHERE name = 'Preventive Health Specialist'),
 (SELECT id FROM contact_types WHERE name = 'Email'), 'john.smith@example.com'),

((SELECT id FROM resumes WHERE name = 'Counseling Psychologist'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '996701123456'),

((SELECT id FROM resumes WHERE name = 'Certified Personal Trainer'),
 (SELECT id FROM contact_types WHERE name = 'Website'), 'https://fitwithmichael.com'),

((SELECT id FROM resumes WHERE name = 'Nutrition Consultant'),
 (SELECT id FROM contact_types WHERE name = 'Telegram'), '@sophiab_nutrition'),

((SELECT id FROM resumes WHERE name = 'Licensed Practical Nurse'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '996704123457'),

((SELECT id FROM resumes WHERE name = 'Meditation Guide'),
 (SELECT id FROM contact_types WHERE name = 'Email'), 'olivia.garcia@example.com'),

((SELECT id FROM resumes WHERE name = 'Physical Therapy Aide'),
 (SELECT id FROM contact_types WHERE name = 'LinkedIn'), 'https://linkedin.com/in/robertmiller-therapy'),

((SELECT id FROM resumes WHERE name = 'Dietetic Technician'),
 (SELECT id FROM contact_types WHERE name = 'Email'), 'ava.davis@example.com'),

((SELECT id FROM resumes WHERE name = 'Wellness Coach'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '996706123457'),

((SELECT id FROM resumes WHERE name = 'Health Educator'),
 (SELECT id FROM contact_types WHERE name = 'Website'), 'https://healthwithdavid.com'),

((SELECT id FROM resumes WHERE name = 'Nutritional Assistant'),
 (SELECT id FROM contact_types WHERE name = 'Telegram'), '@mialopez_nutrition'),

((SELECT id FROM resumes WHERE name = 'Medical Massage Therapist'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '996707123457'),

((SELECT id FROM resumes WHERE name = 'Art Therapist'),
 (SELECT id FROM contact_types WHERE name = 'Email'), 'charlotte.wilson@example.com'),

((SELECT id FROM resumes WHERE name = 'Senior Fitness Specialist'),
 (SELECT id FROM contact_types WHERE name = 'Phone'), '996708123457');