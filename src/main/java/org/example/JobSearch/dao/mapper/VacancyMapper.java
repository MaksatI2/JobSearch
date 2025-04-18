package org.example.JobSearch.dao.mapper;

import org.example.JobSearch.model.Vacancy;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VacancyMapper implements RowMapper<Vacancy> {
    @Override
    public Vacancy mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Vacancy.builder()
                .id(rs.getLong("id"))
//                .authorId(rs.getLong("author_id"))
//                .categoryId(rs.getLong("category_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .salary(rs.getFloat("salary"))
                .expFrom(rs.getInt("exp_from"))
                .expTo(rs.getInt("exp_to"))
                .isActive(rs.getBoolean("is_active"))
                .createdDate(rs.getTimestamp("created_date"))
                .updateTime(rs.getTimestamp("update_time"))
                .build();
    }
}
