package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EditUserDao {
    private final JdbcTemplate jdbcTemplate;

    public int updateUserByEmail(String email, User user) {
        String sql = "UPDATE users SET name = ?, surname = ?, age = ?, phone_number = ? WHERE email = ?";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getSurname(),
                user.getAge(),
                user.getPhoneNumber(),
                email);
    }

    public int deleteUserByEmail(String email) {
        String sql = "DELETE FROM users WHERE email = ?";
        return jdbcTemplate.update(sql, email);
    }
}