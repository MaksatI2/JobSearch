UPDATE users
SET password = '$2a$12$hNG8MgKymiUKmaMXieKD/u4rssrlXGBAqzNhSj8CKEcWQZkDKVtAW' -- временный пароль empty
WHERE email IN (
                'Mark@example.com',
                'Islam@example.com',
                'Ricki@example.com',
                'Jane@example.com',
                'Jane2@example.com'
    );

--Все пароли "qwerty"
UPDATE users SET password = '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.' WHERE email = 'Mark@example.com';
UPDATE users SET password = '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.' WHERE email = 'Islam@example.com';
UPDATE users SET password = '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.' WHERE email = 'Ricki@example.com';
UPDATE users SET password = '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.' WHERE email = 'Jane@example.com';
UPDATE users SET password = '$2a$12$/i0RK0OdHQG/r0IaG1ZVvecGI1Vf4SPZX0OGQK3ysHSsM5ulWNut.' WHERE email = 'Jane2@example.com';