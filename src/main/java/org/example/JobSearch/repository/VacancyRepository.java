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

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    @Query("SELECT v FROM Vacancy v WHERE v.isActive = true " +
           "AND (:categoryId IS NULL OR v.category.id = :categoryId OR v.category.parent.id = :categoryId) " +
           "AND (:regionId IS NULL OR (v.region IS NOT NULL AND v.region.id = :regionId)) " +
           "ORDER BY " +
           "CASE WHEN :sort = 'salaryAsc' THEN v.salary END ASC, " +
           "CASE WHEN :sort = 'salaryDesc' THEN v.salary END DESC, " +
           "CASE WHEN :sort = 'expAsc' THEN v.expFrom END ASC, " +
           "CASE WHEN :sort = 'expDesc' THEN v.expFrom END DESC, " +
           "CASE WHEN :sort = 'responsesDesc' THEN (SELECT COUNT(ra) FROM RespondedApplicant ra WHERE ra.vacancy = v) END DESC, " +
           "CASE WHEN :sort = 'regionAsc' THEN v.region.name END ASC, " +
           "CASE WHEN :sort = 'regionDesc' THEN v.region.name END DESC, " +
           "v.updateTime DESC")
    Page<Vacancy> findAllActiveSorted(@Param("sort") String sort,
                                      @Param("categoryId") Long categoryId,
                                      @Param("regionId") Long regionId,
                                      Pageable pageable);


    @Query("SELECT v FROM Vacancy v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :query, '%')) AND v.isActive = true")
    Page<Vacancy> searchByNameIgnoreCase(@Param("query") String query, Pageable pageable);

    @Query("""
    SELECT DISTINCT v FROM Vacancy v 
    JOIN v.applicants ra
    WHERE v.author.id = :userId
    GROUP BY v
    HAVING COUNT(ra) > 0
""")
    Page<Vacancy> findVacanciesWithResponsesByAuthorId(@Param("userId") Long userId, Pageable pageable);

    boolean existsById(Long id);

    Page<Vacancy> findByAuthorId(Long employerId, Pageable pageable);

    @Modifying
    @Query("UPDATE Vacancy v SET v.updateTime = :updateTime WHERE v.id = :id")
    void updateUpdateTime(@Param("id") Long id, @Param("updateTime") Timestamp updateTime);

    default void refreshVacancy(Long id) {
        updateUpdateTime(id, new Timestamp(System.currentTimeMillis()));
    }
}