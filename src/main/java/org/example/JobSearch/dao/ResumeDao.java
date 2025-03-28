package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.mapper.ResumeMapper;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.exceptions.ResumeNotFoundException;
import org.example.JobSearch.model.Resume;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResumeDao {
    private final JdbcTemplate jdbcTemplate;

    public Long createResume(ResumeDTO resume) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                    INSERT INTO resumes (applicant_id, category_id, name, salary, is_active, update_time, create_date)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, resume.getApplicantId());
            ps.setLong(2, resume.getCategoryId());
            ps.setString(3, resume.getName());
            ps.setFloat(4, resume.getSalary());
            ps.setBoolean(5, resume.getIsActive());
            ps.setTimestamp(6, resume.getUpdateTime());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void updateResume(Long id, ResumeDTO resume) {
        String sql = """
                    UPDATE resumes 
                    SET applicant_id = ?, category_id = ?, name = ?, salary = ?, is_active = ?, update_time = ?
                    WHERE id = ?
                """;
        jdbcTemplate.update(sql, resume.getApplicantId(), resume.getCategoryId(), resume.getName(),
                resume.getSalary(), resume.getIsActive(), resume.getUpdateTime(), id);
    }

    public void deleteResume(Long id) {
        String sql = "DELETE FROM resumes WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Resume> getAllActiveResumes() {
        String sql = "SELECT * FROM resumes WHERE is_active = TRUE";
        return jdbcTemplate.query(sql, new ResumeMapper());
    }

    public List<Resume> getActiveResumesByCategory(Long categoryId) {
        String sql = """
                    WITH RECURSIVE category_tree(id) AS (
                        SELECT id FROM categories WHERE id = ?
                        UNION ALL
                        SELECT c.id FROM categories c
                        JOIN category_tree ct ON c.parent_id = ct.id
                    )
                    SELECT * FROM resumes WHERE category_id IN 
                    (SELECT id FROM category_tree)
                    AND is_active = TRUE;
                """;
        return jdbcTemplate.query(sql, new ResumeMapper(), categoryId);
    }

    public Resume getResumeById(Long id) {
        String sql = "SELECT * FROM resumes WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new ResumeMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResumeNotFoundException("Resume not found with ID: " + id);
        }
    }

    public Boolean existsResume(Long id) {
        String sql = "select count(*) from resumes where ID = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);
        return count != null && count > 0;
    }

    public List<Resume> getUserResumes(Long applicant_id) {
        String sql = "SELECT * FROM resumes WHERE applicant_id = ?";
        return jdbcTemplate.query(sql, new ResumeMapper(), applicant_id);
    }
}
