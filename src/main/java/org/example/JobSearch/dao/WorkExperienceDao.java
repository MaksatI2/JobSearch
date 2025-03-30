package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.mapper.WorkExperienceMapper;
import org.example.JobSearch.model.WorkExperience;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WorkExperienceDao {
    private final JdbcTemplate jdbcTemplate;

    public void createWorkExperience(Long resumeId, WorkExperience workExperience) {
        String sql = """
        INSERT INTO work_experience_info (resume_id, years, company_name, position, responsibilities)
        VALUES (?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql,
                resumeId,
                workExperience.getYears(),
                workExperience.getCompanyName(),
                workExperience.getPosition(),
                workExperience.getResponsibilities()
        );
    }

    public List<WorkExperience> getWorkExperienceByResumeId(Long resumeId) {
        String sql = "SELECT * FROM work_experience_info WHERE resume_id = ?";
        return jdbcTemplate.query(sql, new WorkExperienceMapper(), resumeId);
    }

    public void updateWorkExperience(Long workExperienceId, WorkExperience workExperience) {
        String sql = """
    INSERT work_experience_info
    SET years = ?, company_name = ?, position = ?, responsibilities = ?
    WHERE id = ?
    """;
        jdbcTemplate.update(sql,
                workExperience.getYears(),
                workExperience.getCompanyName(),
                workExperience.getPosition(),
                workExperience.getResponsibilities(),
                workExperienceId
        );
    }

    public void deleteWorkExperience(Long id) {
        String sql = "DELETE FROM work_experience_info WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}