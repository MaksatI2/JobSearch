package org.example.JobSearch.dao;

import org.example.JobSearch.dao.mapper.UserMapper;
import org.example.JobSearch.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

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

    public Optional<User> findEmployer(String email) {
        String sql = "SELECT u.* FROM users u JOIN authorities a ON u.account_type = a.id " +
                "WHERE u.email = ? AND a.authority = 'EMPLOYER'";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), email))
        );
    }

    public Optional<User> findEmployerByPhone(String phoneNumber) {
        String sql = "SELECT u.* FROM users u JOIN authorities a ON u.account_type = a.id " +
                "WHERE u.phone_number = ? AND a.authority = 'EMPLOYER'";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), phoneNumber))
        );
    }

    public List<User> findEmployersByName(String name) {
        String sql = "SELECT u.* FROM users u JOIN authorities a ON u.account_type = a.id " +
                "WHERE u.name = ? AND a.authority = 'EMPLOYER'";
        return jdbcTemplate.query(sql, new UserMapper(), name);
    }

    public Optional<User> findApplicant(String email) {
        String sql = "SELECT u.* FROM users u JOIN authorities a ON u.account_type = a.id " +
                "WHERE u.email = ? AND a.authority = 'APPLICANT'";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), email))
        );
    }

    public Optional<User> findApplicantByPhone(String phoneNumber) {
        String sql = "SELECT u.* FROM users u JOIN authorities a ON u.account_type = a.id " +
                "WHERE u.phone_number = ? AND a.authority = 'APPLICANT'";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), phoneNumber))
        );
    }

    public List<User> findApplicantsByName(String name) {
        String sql = "SELECT u.* FROM users u JOIN authorities a ON u.account_type = a.id " +
                "WHERE u.name = ? AND a.authority = 'APPLICANT'";
        return jdbcTemplate.query(sql, new UserMapper(), name);
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public List<User> findApplicantsByVacancyId(Long vacancyId) {
        String sql = """
        SELECT u.* FROM users u
        JOIN resumes r ON u.id = r.applicant_id
        JOIN responded_applicants ra ON r.id = ra.resume_id
        WHERE ra.vacancy_id = ?
    """;
        return jdbcTemplate.query(sql, new UserMapper(), vacancyId);
    }

    public void save(User user) {
        String sql = "INSERT INTO users (email, name, surname, age, password, phone_number, avatar,  account_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getName(), user.getSurname(),
                user.getAge(), user.getPassword(), user.getPhoneNumber(), user.getAvatar(),
                user.getAccountType().getId());
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), email))
        );
    }

    public Optional<String> findAvatarPathById(Long userId) {
        String sql = "SELECT avatar FROM users WHERE id = ?";
        try {
            String avatarPath = jdbcTemplate.queryForObject(sql, String.class, userId);
            return Optional.ofNullable(avatarPath);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean isUserEmployer(Long userId) {
        String sql = "SELECT COUNT(*) FROM users u JOIN authorities a ON u.account_type = a.id " +
                "WHERE u.id = ? AND a.authority = 'EMPLOYER'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    public boolean isUserApplicant(Long userId) {
        String sql = "SELECT COUNT(*) FROM users u JOIN authorities a ON u.account_type = a.id " +
                "WHERE u.id = ? AND a.authority = 'APPLICANT'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }
}


