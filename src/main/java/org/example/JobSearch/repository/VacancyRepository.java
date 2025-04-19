package org.example.JobSearch.repository;

import org.example.JobSearch.model.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    @Query("SELECT v FROM Vacancy v WHERE v.isActive = true " +
            "ORDER BY " +
            "CASE WHEN :sort = 'salaryAsc' THEN v.salary END ASC, " +
            "CASE WHEN :sort = 'salaryDesc' THEN v.salary END DESC, " +
            "CASE WHEN :sort = 'expAsc' THEN v.expFrom END ASC, " +
            "CASE WHEN :sort = 'expDesc' THEN v.expFrom END DESC, " +
            "v.updateTime DESC")
    Page<Vacancy> findAllActiveSorted(@Param("sort") String sort, Pageable pageable);

    long countByIsActiveTrue();

    @Query(value = """
        WITH RECURSIVE category_tree(id) AS (
            SELECT id FROM categories WHERE id = :categoryId
            UNION ALL
            SELECT c.id FROM categories c
            JOIN category_tree ct ON c.parent_id = ct.id
        )
        SELECT v.* FROM vacancies v WHERE v.category_id IN 
        (SELECT id FROM category_tree)
        AND v.is_active = TRUE
    """, nativeQuery = true)
    List<Vacancy> findActiveByCategoryTree(@Param("categoryId") Long categoryId);

    @Query("""
        SELECT v FROM Vacancy v
        JOIN v.applicants ra
        JOIN ra.resume r
        WHERE r.applicant.id = :applicantId
    """)
    List<Vacancy> findRespondedByApplicantId(@Param("applicantId") Long applicantId);

    boolean existsById(Long id);

    Page<Vacancy> findByAuthorId(Long employerId, Pageable pageable);

    @Modifying
    @Query("UPDATE Vacancy v SET v.updateTime = :updateTime WHERE v.id = :id")
    void updateUpdateTime(@Param("id") Long id, @Param("updateTime") Timestamp updateTime);

    default void refreshVacancy(Long id) {
        updateUpdateTime(id, new Timestamp(System.currentTimeMillis()));
    }
}