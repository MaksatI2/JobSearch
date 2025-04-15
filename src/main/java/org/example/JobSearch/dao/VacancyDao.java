package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.mapper.VacancyMapper;
import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.model.Vacancy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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

    public void updateVacancy(Long id, EditVacancyDTO vacancy) {
        String sql = """
            UPDATE vacancies 
            SET category_id = ?, name = ?, description = ?, salary = ?, exp_from = ?, exp_to = ?, is_active = ?, update_time = ?
            WHERE id = ?
        """;
        jdbcTemplate.update(sql, vacancy.getCategoryId(), vacancy.getName(),
                vacancy.getDescription(), vacancy.getSalary(), vacancy.getExpFrom(), vacancy.getExpTo(),
                vacancy.getIsActive(), vacancy.getUpdateTime(), id);
    }

    public void deleteVacancy(Long id) {
        String sql = "DELETE FROM vacancies WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Vacancy> getAllVacancies(Integer page, Integer size) {
        String sql = "SELECT * FROM vacancies WHERE is_active = TRUE LIMIT ? OFFSET ?";
        int offset = (page - 1) * size;
        return jdbcTemplate.query(sql, new VacancyMapper(), size, offset);
    }

    public int countActiveVacancies() {
        String sql = "SELECT COUNT(*) FROM vacancies WHERE is_active = TRUE";
        return jdbcTemplate.queryForObject(sql, Integer.class);
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

    public Boolean existsVacancy(Long id) {
        String sql = "select count(*) from vacancies where ID = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);
        return count != null && count > 0;
    }

    public List<Vacancy> getVacanciesByEmployer(Long employerId) {
        String sql = "SELECT * FROM vacancies WHERE author_id = ?";
        return jdbcTemplate.query(sql, new VacancyMapper(), employerId);
    }

    public Vacancy getVacancyById(Long id) {
        String sql = "SELECT * FROM vacancies WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, new VacancyMapper(), id);
    }

    public void refreshVacancy(Long id) {
        String sql = "UPDATE vacancies SET update_time = ? WHERE id = ?";
        jdbcTemplate.update(sql, new Timestamp(System.currentTimeMillis()), id);
    }

}
