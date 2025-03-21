package org.example.JobSearch.dao.mapper;

import org.example.JobSearch.model.Resume;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResumeMapper implements RowMapper<Resume> {
    @Override
    public Resume mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Resume.builder()
                .id(rs.getLong("id"))
                .applicantId(rs.getLong("applicant_id"))
                .categoryId(rs.getLong("category_id"))
                .name(rs.getString("name"))
                .salary(rs.getFloat("salary"))
                .isActive(rs.getBoolean("is_active"))
                .updateTime(rs.getTimestamp("update_time"))
                .build();
    }
}
