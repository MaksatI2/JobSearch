    package org.example.JobSearch.dao;

    import lombok.RequiredArgsConstructor;
    import org.example.JobSearch.dao.mapper.CategoryMapper;
    import org.example.JobSearch.model.Category;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    @RequiredArgsConstructor
    public class CategoryDao {
        private final JdbcTemplate jdbcTemplate;

        public List<Category> findAll() {
            String sql = "SELECT * FROM categories";
            return jdbcTemplate.query(sql, new CategoryMapper());
        }

        public boolean existsById(Long categoryId) {
            String sql = "SELECT COUNT(*) FROM categories WHERE id = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
            return count != null && count > 0;
        }
    }

