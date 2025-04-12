INSERT INTO categories (name, parent_id)
VALUES
    ('Physical Health', NULL),
    ('Mental Wellbeing', NULL),
    ('Nutrition', NULL),
    ('Fitness', NULL),

    ('Preventive Care', 1),
    ('Medical Treatments', 1),
    ('Stress Management', 2),
    ('Mindfulness Practices', 2),
    ('Healthy Diets', 3),
    ('Exercise Programs', 4);

INSERT INTO contact_types (name)
VALUES ('Phone'),
       ('Email'),
       ('LinkedIn'),
       ('GitHub'),
       ('Telegram'),
       ('Website');