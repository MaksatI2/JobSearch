UPDATE users
SET account_type = (SELECT id FROM authorities WHERE authority = 'APPLICANT')
WHERE email = 'Mark@example.com';

UPDATE users
SET account_type = (SELECT id FROM authorities WHERE authority = 'APPLICANT')
WHERE email = 'Islam@example.com';

UPDATE users
SET account_type = (SELECT id FROM authorities WHERE authority = 'EMPLOYER')
WHERE email = 'Ricki@example.com';

UPDATE users
SET account_type = (SELECT id FROM authorities WHERE authority = 'EMPLOYER')
WHERE email = 'Jane@example.com';

UPDATE users
SET account_type = (SELECT id FROM authorities WHERE authority = 'EMPLOYER')
WHERE email = 'Jane2@example.com';
