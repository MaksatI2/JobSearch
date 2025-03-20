package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.mapper.VacancyMapper;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.model.Vacancy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacancyDao {
    private final JdbcTemplate jdbcTemplate;

    public void createVacancy(VacancyDTO vacancy) {
        String sql = """
            INSERT INTO vacancies (author_id, category_id, name, description, salary, exp_from, exp_to, is_active, created_date, update_time)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql, vacancy.getAuthorId(), vacancy.getCategoryId(), vacancy.getName(),
                vacancy.getDescription(), vacancy.getSalary(), vacancy.getExpFrom(), vacancy.getExpTo(),
                vacancy.getIsActive(), vacancy.getCreatedDate(), vacancy.getUpdateTime());
    }

    public void updateVacancy(Long id, VacancyDTO vacancy) {
        String sql = """
            UPDATE vacancies 
            SET author_id = ?, category_id = ?, name = ?, description = ?, salary = ?, exp_from = ?, exp_to = ?, is_active = ?, update_time = ?
            WHERE id = ?
        """;
        jdbcTemplate.update(sql, vacancy.getAuthorId(), vacancy.getCategoryId(), vacancy.getName(),
                vacancy.getDescription(), vacancy.getSalary(), vacancy.getExpFrom(), vacancy.getExpTo(),
                vacancy.getIsActive(), vacancy.getUpdateTime(), id);
    }

    public void deleteVacancy(Long id) {
        String sql = "DELETE FROM vacancies WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Vacancy> getAllVacancies() {
        String sql = "SELECT * FROM vacancies WHERE is_active = TRUE";
        return jdbcTemplate.query(sql, new VacancyMapper());
    }

    public List<Vacancy> getVacanciesByCategory(Long categoryId) {
        String sql = "SELECT * FROM vacancies WHERE category_id = ? AND is_active = TRUE";
        return jdbcTemplate.query(sql, new VacancyMapper(), categoryId);
    }
}
