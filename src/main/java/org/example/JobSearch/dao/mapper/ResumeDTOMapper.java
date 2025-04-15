package org.example.JobSearch.dao.mapper;

import org.example.JobSearch.dto.ResumeDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResumeDTOMapper implements RowMapper<ResumeDTO> {
    @Override
    public ResumeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ResumeDTO.builder()
                .id(rs.getLong("id"))
                .applicantId(rs.getLong("applicant_id"))
                .categoryId(rs.getLong("category_id"))
                .name(rs.getString("name"))
                .salary(rs.getFloat("salary"))
                .isActive(rs.getBoolean("is_active"))
                .createDate(rs.getTimestamp("create_date"))
                .updateTime(rs.getTimestamp("update_time"))
                .build();
    }
}
