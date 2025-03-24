package org.example.JobSearch.dao;

import org.example.JobSearch.dao.mapper.UserMapper;
import org.example.JobSearch.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public Optional<User> findEmployer(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND account_type = 'EMPLOYER'";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), email))
        );
    }

    public Optional<User> findEmployerByPhone(String phoneNumber) {
        String sql = "SELECT * FROM users WHERE phone_number = ? AND account_type = 'EMPLOYER'";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), phoneNumber))
        );
    }

    public List<User> findEmployersByName(String name) {
        String sql = "SELECT * FROM users WHERE name = ? AND account_type = 'EMPLOYER'";
        return jdbcTemplate.query(sql, new UserMapper(), name);
    }

    public Optional<User> findApplicant(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND account_type = 'APPLICANT'";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), email))
        );
    }

    public Optional<User> findApplicantByPhone(String phoneNumber) {
        String sql = "SELECT * FROM users WHERE phone_number = ? AND account_type = 'APPLICANT'";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), phoneNumber))
        );
    }

    public List<User> findApplicantsByName(String name) {
        String sql = "SELECT * FROM users WHERE name = ? AND account_type = 'APPLICANT'";
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
        String sql = "INSERT INTO users (email, name, surname, age, password, phone_number, avatar, account_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getName(), user.getSurname(),
                user.getAge(), user.getPassword(), user.getPhoneNumber(),
                user.getAvatar(), user.getAccountType());
    }


    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql, new UserMapper(), email))
        );
    }
}


