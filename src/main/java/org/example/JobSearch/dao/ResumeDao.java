package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.mapper.ResumeMapper;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.model.Resume;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResumeDao {
    private final JdbcTemplate jdbcTemplate;

    public void createResume(ResumeDTO resume) {
        String sql = """
        INSERT INTO resumes (applicant_id, category_id, name, salary, is_active, update_time)
        VALUES (?, ?, ?, ?, ?, ?)
    """;
        jdbcTemplate.update(sql,
                resume.getApplicantId(),
                resume.getCategoryId(),
                resume.getName(),
                resume.getSalary(),
                resume.getIsActive(),
                resume.getUpdateTime()
        );
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
        String sql = "SELECT * FROM resumes WHERE category_id = ? AND is_active = TRUE";
        return jdbcTemplate.query(sql, new ResumeMapper(), categoryId);
    }

    public List<Resume> getUserResumes(Long applicant_id) {
        String sql = "SELECT * FROM resumes WHERE applicant_id = ?";
        return jdbcTemplate.query(sql, new ResumeMapper(), applicant_id);
    }
}
