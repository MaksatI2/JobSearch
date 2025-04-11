package org.example.JobSearch.dao.mapper;

import org.example.JobSearch.model.RespondedApplicant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RespondedApplicantRowMapper implements RowMapper<RespondedApplicant> {
    @Override
    public RespondedApplicant mapRow(ResultSet rs, int rowNum) throws SQLException {
        RespondedApplicant applicant = new RespondedApplicant();
        applicant.setId(rs.getLong("id"));
        applicant.setResumeId(rs.getLong("resume_id"));
        applicant.setVacancyId(rs.getLong("vacancy_id"));
        applicant.setConfirmation(rs.getBoolean("confirmation"));
        return applicant;
    }
}
