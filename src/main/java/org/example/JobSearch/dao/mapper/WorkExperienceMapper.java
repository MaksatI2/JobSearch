package org.example.JobSearch.dao.mapper;

import org.example.JobSearch.model.WorkExperience;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkExperienceMapper implements RowMapper<WorkExperience> {
    @Override
    public WorkExperience mapRow(ResultSet rs, int rowNum) throws SQLException {
        WorkExperience workExperience = new WorkExperience();
        workExperience.setId(rs.getLong("id"));
        workExperience.setResumeId(rs.getLong("resume_id"));
        workExperience.setYears(rs.getLong("years"));
        workExperience.setCompanyName(rs.getString("company_name"));
        workExperience.setPosition(rs.getString("position"));
        workExperience.setResponsibilities(rs.getString("responsibilities"));
        return workExperience;
    }
}