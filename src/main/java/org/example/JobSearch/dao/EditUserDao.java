package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.mapper.UserMapper;
import org.example.JobSearch.model.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    public int updateUserAvatar(Long userId, String avatarPath) {
        String sql = "UPDATE users SET avatar = ? WHERE id = ?";
        return jdbcTemplate.update(sql, avatarPath, userId);
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), id))
        );
    }
}