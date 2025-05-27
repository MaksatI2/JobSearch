-- changeset I.Maksat:037

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Providing preventive medical services and maintaining documentation. Must have nursing certification.'
WHERE name = 'Preventive Care Nurse';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'Чуйская область'),
                     responsibilities = 'Performing physiotherapy procedures. Experience with rehabilitation required.'
WHERE name = 'Physiotherapist';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Providing stress management counseling. Psychology degree required.'
WHERE name = 'Stress Management Psychologist';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'Иссык-Кульская область'),
                     responsibilities = 'Conducting group and individual meditation sessions. Certification preferred.'
WHERE name = 'Meditation Instructor';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Developing personalized meal plans. Nutrition degree required.'
WHERE name = 'Nutritionist';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'Ошская область'),
                     responsibilities = 'Providing healthy eating advice. 1+ years experience in nutrition.'
WHERE name = 'Nutrition Consultant';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Conducting one-on-one training sessions. Fitness certification required.'
WHERE name = 'Personal Trainer';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'Чуйская область'),
                     responsibilities = 'Leading group exercise classes. Experience with group instruction.'
WHERE name = 'Group Fitness Instructor';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Conducting art therapy sessions. Art therapy certification preferred.'
WHERE name = 'Art Therapist';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'Джалал-Абадская область'),
                     responsibilities = 'Providing general patient care. Nursing degree required.'
WHERE name = 'Nurse';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Performing therapeutic massage. Massage license required.'
WHERE name = 'Massage Therapist';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Preparing nutritious meals. Culinary experience required.'
WHERE name = 'Healthy Chef';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'Иссык-Кульская область'),
                     responsibilities = 'Teaching yoga classes. Yoga instructor certification required.'
WHERE name = 'Yoga Instructor';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Providing one-on-one coaching sessions. 5+ years experience.'
WHERE name = 'Stress Management Coach';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'Чуйская область'),
                     responsibilities = 'Conducting primary patient consultations. Medical license required.'
WHERE name = 'General Practitioner';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Teaching proper breathing techniques. Certification preferred.'
WHERE name = 'Breathing Techniques Instructor';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Conducting functional training sessions. Fitness certification required.'
WHERE name = 'Functional Training Coach';

INSERT INTO vacancy_employment_types (vacancy_id, employment_type_id)
VALUES
    ((SELECT id FROM vacancies WHERE name = 'Preventive Care Nurse'), (SELECT id FROM employment_types WHERE name = 'Полная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Physiotherapist'), (SELECT id FROM employment_types WHERE name = 'Полная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Stress Management Psychologist'), (SELECT id FROM employment_types WHERE name = 'Частичная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Meditation Instructor'), (SELECT id FROM employment_types WHERE name = 'Частичная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Nutritionist'), (SELECT id FROM employment_types WHERE name = 'Полная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Nutrition Consultant'), (SELECT id FROM employment_types WHERE name = 'Проект/разовое задание')),
    ((SELECT id FROM vacancies WHERE name = 'Personal Trainer'), (SELECT id FROM employment_types WHERE name = 'Частичная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Group Fitness Instructor'), (SELECT id FROM employment_types WHERE name = 'Частичная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Art Therapist'), (SELECT id FROM employment_types WHERE name = 'Полная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Nurse'), (SELECT id FROM employment_types WHERE name = 'Вахта')),
    ((SELECT id FROM vacancies WHERE name = 'Massage Therapist'), (SELECT id FROM employment_types WHERE name = 'Полная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Healthy Chef'), (SELECT id FROM employment_types WHERE name = 'Полная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Yoga Instructor'), (SELECT id FROM employment_types WHERE name = 'Частичная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Stress Management Coach'), (SELECT id FROM employment_types WHERE name = 'Проект/разовое задание')),
    ((SELECT id FROM vacancies WHERE name = 'General Practitioner'), (SELECT id FROM employment_types WHERE name = 'Полная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Breathing Techniques Instructor'), (SELECT id FROM employment_types WHERE name = 'Частичная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Functional Training Coach'), (SELECT id FROM employment_types WHERE name = 'Полная занятость'));

INSERT INTO vacancy_work_schedules (vacancy_id, work_schedule_id)
VALUES
    ((SELECT id FROM vacancies WHERE name = 'Preventive Care Nurse'), (SELECT id FROM work_schedules WHERE name = 'Полный день')),
    ((SELECT id FROM vacancies WHERE name = 'Physiotherapist'), (SELECT id FROM work_schedules WHERE name = 'Полный день')),
    ((SELECT id FROM vacancies WHERE name = 'Stress Management Psychologist'), (SELECT id FROM work_schedules WHERE name = 'Удаленно')),
    ((SELECT id FROM vacancies WHERE name = 'Meditation Instructor'), (SELECT id FROM work_schedules WHERE name = 'Гибрид')),
    ((SELECT id FROM vacancies WHERE name = 'Nutritionist'), (SELECT id FROM work_schedules WHERE name = 'Полный день')),
    ((SELECT id FROM vacancies WHERE name = 'Nutrition Consultant'), (SELECT id FROM work_schedules WHERE name = 'Удаленно')),
    ((SELECT id FROM vacancies WHERE name = 'Personal Trainer'), (SELECT id FROM work_schedules WHERE name = 'Разъездной')),
    ((SELECT id FROM vacancies WHERE name = 'Group Fitness Instructor'), (SELECT id FROM work_schedules WHERE name = 'Разъездной')),
    ((SELECT id FROM vacancies WHERE name = 'Art Therapist'), (SELECT id FROM work_schedules WHERE name = 'Полный день')),
    ((SELECT id FROM vacancies WHERE name = 'Nurse'), (SELECT id FROM work_schedules WHERE name = 'Полный день')),
    ((SELECT id FROM vacancies WHERE name = 'Massage Therapist'), (SELECT id FROM work_schedules WHERE name = 'Гибрид')),
    ((SELECT id FROM vacancies WHERE name = 'Healthy Chef'), (SELECT id FROM work_schedules WHERE name = 'Полный день')),
    ((SELECT id FROM vacancies WHERE name = 'Yoga Instructor'), (SELECT id FROM work_schedules WHERE name = 'Разъездной')),
    ((SELECT id FROM vacancies WHERE name = 'Stress Management Coach'), (SELECT id FROM work_schedules WHERE name = 'Удаленно')),
    ((SELECT id FROM vacancies WHERE name = 'General Practitioner'), (SELECT id FROM work_schedules WHERE name = 'Полный день')),
    ((SELECT id FROM vacancies WHERE name = 'Breathing Techniques Instructor'), (SELECT id FROM work_schedules WHERE name = 'Гибрид')),
    ((SELECT id FROM vacancies WHERE name = 'Functional Training Coach'), (SELECT id FROM work_schedules WHERE name = 'Полный день'));

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Develop software applications using modern technologies. Knowledge of Java or Python required.'
WHERE name = 'Software Developer';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Manage software projects from conception to delivery. PMP certification preferred.'
WHERE name = 'Project Manager';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'Чуйская область'),
                     responsibilities = 'Develop backend systems for mobile games. Experience with Unity or Unreal Engine.'
WHERE name = 'Backend Junior Developer';

UPDATE vacancies SET
                     region_id = (SELECT id FROM regions WHERE name = 'город Бишкек'),
                     responsibilities = 'Develop responsive websites using modern frameworks. Proficiency in React or Angular required.'
WHERE name = 'Frontend Developer';

INSERT INTO vacancy_employment_types (vacancy_id, employment_type_id)
VALUES
    ((SELECT id FROM vacancies WHERE name = 'Software Developer'), (SELECT id FROM employment_types WHERE name = 'Полная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Project Manager'), (SELECT id FROM employment_types WHERE name = 'Полная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Backend Junior Developer'), (SELECT id FROM employment_types WHERE name = 'Полная занятость')),
    ((SELECT id FROM vacancies WHERE name = 'Frontend Developer'), (SELECT id FROM employment_types WHERE name = 'Частичная занятость'));

INSERT INTO vacancy_work_schedules (vacancy_id, work_schedule_id)
VALUES
    ((SELECT id FROM vacancies WHERE name = 'Software Developer'), (SELECT id FROM work_schedules WHERE name = 'Полный день')),
    ((SELECT id FROM vacancies WHERE name = 'Project Manager'), (SELECT id FROM work_schedules WHERE name = 'Гибрид')),
    ((SELECT id FROM vacancies WHERE name = 'Backend Junior Developer'), (SELECT id FROM work_schedules WHERE name = 'Полный день')),
    ((SELECT id FROM vacancies WHERE name = 'Frontend Developer'), (SELECT id FROM work_schedules WHERE name = 'Удаленно'));