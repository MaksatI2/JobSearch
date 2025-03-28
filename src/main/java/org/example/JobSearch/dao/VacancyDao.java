package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.mapper.VacancyMapper;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.exceptions.ResumeNotFoundException;
import org.example.JobSearch.model.Vacancy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacancyDao {
    private final JdbcTemplate jdbcTemplate;

    public void createVacancy(VacancyDTO vacancy) {
        String sql = """
            INSERT INTO vacancies (author_id, category_id, name, description, salary, exp_from, exp_to, is_active, update_time)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql, vacancy.getAuthorId(), vacancy.getCategoryId(), vacancy.getName(),
                vacancy.getDescription(), vacancy.getSalary(), vacancy.getExpFrom(), vacancy.getExpTo(),
                vacancy.getIsActive(), vacancy.getUpdateTime());
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
        String sql = """
        WITH RECURSIVE category_tree(id) AS (
            SELECT id FROM categories WHERE id = ?
            UNION ALL
            SELECT c.id FROM categories c
            JOIN category_tree ct ON c.parent_id = ct.id
        )
        SELECT * FROM vacancies WHERE category_id IN 
        (SELECT id FROM category_tree)
        AND is_active = TRUE;
    """;
        return jdbcTemplate.query(sql, new VacancyMapper(), categoryId);
    }

    public List<Vacancy> getRespondedVacancies(Long applicantId) {
        String sql = """
        SELECT v.* FROM vacancies v
        JOIN responded_applicants ra ON v.id = ra.vacancy_id
        JOIN resumes r ON ra.resume_id = r.id
        WHERE r.applicant_id = ?
    """;
        return jdbcTemplate.query(sql, new VacancyMapper(), applicantId);
    }

    public Vacancy getVacancyById(Long id) {
        String sql = "SELECT * FROM vacancies WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new VacancyMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResumeNotFoundException("Resume not found with ID: " + id);
        }
    }

}
