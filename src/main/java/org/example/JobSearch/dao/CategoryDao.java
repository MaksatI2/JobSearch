package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.mapper.CategoryMapper;
import org.example.JobSearch.dao.mapper.ResumeMapper;
import org.example.JobSearch.dao.mapper.VacancyMapper;
import org.example.JobSearch.exceptions.CategoryNotFoundException;
import org.example.JobSearch.model.Category;
import org.example.JobSearch.model.Resume;
import org.example.JobSearch.model.Vacancy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryDao {
    private final JdbcTemplate jdbcTemplate;

    public List<Category> findAll() {
        String sql = "SELECT * FROM categories";
        return jdbcTemplate.query(sql, new CategoryMapper());
    }

    public Optional<Category> findById(Long id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try {
            Category category = jdbcTemplate.queryForObject(sql, new CategoryMapper(), id);
            return Optional.ofNullable(category);
        } catch (CategoryNotFoundException e) {
            return Optional.empty();
        }
    }

    public List<Vacancy> getVacanciesByCategory(Long categoryId) {
        String sql = """
            WITH RECURSIVE category_tree(id) AS (
                SELECT id FROM categories WHERE id = ?
                UNION ALL
                SELECT c.id FROM categories c
                JOIN category_tree ct ON c.parent_id = ct.id
            )
            SELECT * FROM vacancies 
            WHERE category_id IN (SELECT id FROM category_tree)
            AND is_active = TRUE;
        """;
        return jdbcTemplate.query(sql, new VacancyMapper(), categoryId);
    }

    public List<Resume> getResumesByCategory(Long categoryId) {
        String sql = """
            WITH RECURSIVE category_tree(id) AS (
                SELECT id FROM categories WHERE id = ?
                UNION ALL
                SELECT c.id FROM categories c
                JOIN category_tree ct ON c.parent_id = ct.id
            )
            SELECT * FROM resumes 
            WHERE category_id IN (SELECT id FROM category_tree)
            AND is_active = TRUE;
        """;
        return jdbcTemplate.query(sql, new ResumeMapper(), categoryId);
    }

    public boolean existsById(Long categoryId) {
        String sql = "SELECT COUNT(*) FROM categories WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
        return count != null && count > 0;
    }
}

