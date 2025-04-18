package org.example.JobSearch.repository;

import org.example.JobSearch.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByIsActiveTrue();

    List<Resume> findByCategoryIdAndIsActiveTrue(Long categoryId);

    List<Resume> findByApplicantId(Long applicantId);

    boolean existsById(Long id);

    @Query(value = """
        WITH RECURSIVE category_tree(id) AS (
            SELECT id FROM categories WHERE id = :categoryId
            UNION ALL
            SELECT c.id FROM categories c
            JOIN category_tree ct ON c.parent_id = ct.id
        )
        SELECT r.* FROM resumes r 
        WHERE r.category_id IN (SELECT id FROM category_tree)
        AND r.is_active = TRUE
        """, nativeQuery = true)
    List<Resume> findActiveResumesInCategoryTree(@Param("categoryId") Long categoryId);

    @Modifying
    @Query("UPDATE Resume r SET r.updateTime = :updateTime WHERE r.id = :id")
    void updateUpdateTime(@Param("id") Long id, @Param("updateTime") Timestamp updateTime);

    default void refreshResume(Long id) {
        updateUpdateTime(id, new Timestamp(System.currentTimeMillis()));
    }
}