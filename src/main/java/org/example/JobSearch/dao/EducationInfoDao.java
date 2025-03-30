package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.mapper.EducationInfoMapper;
import org.example.JobSearch.model.EducationInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EducationInfoDao {
    private final JdbcTemplate jdbcTemplate;

    public void createEducationInfo(Long resumeId, EducationInfo educationInfo) {
        String sql = """
        INSERT INTO education_info (resume_id, institution_name, program, start_date, end_date, degree)
        VALUES (?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql,
                resumeId,
                educationInfo.getInstitution(),
                educationInfo.getProgram(),
                educationInfo.getStartDate(),
                educationInfo.getEndDate(),
                educationInfo.getDegree()
        );
    }

    public List<EducationInfo> getEducationInfoByResumeId(Long resumeId) {
        String sql = "SELECT * FROM education_info WHERE resume_id = ?";
        return jdbcTemplate.query(sql, new EducationInfoMapper(), resumeId);
    }

    public void updateEducationInfo(Long id, EducationInfo educationInfo) {
        String sql = """
        INSERT education_info 
        SET institution_name = ?, program = ?, start_date = ?, end_date = ?, degree = ?
        WHERE id = ?
        """;
        jdbcTemplate.update(sql,
                educationInfo.getInstitution(),
                educationInfo.getProgram(),
                educationInfo.getStartDate(),
                educationInfo.getEndDate(),
                educationInfo.getDegree(),
                id);
    }

    public void deleteEducationInfo(Long id) {
        String sql = "DELETE FROM education_info WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}