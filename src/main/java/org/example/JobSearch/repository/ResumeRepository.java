package org.example.JobSearch.repository;

import org.example.JobSearch.model.Resume;
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
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    @Query("SELECT r FROM Resume r WHERE r.isActive = true " +
            "ORDER BY " +
            "CASE WHEN :sort = 'salaryAsc' THEN r.salary END ASC, " +
            "CASE WHEN :sort = 'salaryDesc' THEN r.salary END DESC," +
            "r.updateTime DESC")
    Page<Resume> findAllActiveSorted(@Param("sort") String sort, Pageable pageable);

    Page<Resume> findByApplicantId(Long applicantId, Pageable pageable);

    @Query("""
    SELECT DISTINCT r FROM Resume r 
    JOIN r.respondedApplicants resp
    WHERE r.applicant.id = :applicantId
    GROUP BY r
    HAVING COUNT(resp) > 0
""")
    Page<Resume> findResumesWithResponsesByApplicantId(@Param("applicantId") Long applicantId, Pageable pageable);

    boolean existsById(Long id);

    @Modifying
    @Query("UPDATE Resume r SET r.updateTime = :updateTime WHERE r.id = :id")
    void updateUpdateTime(@Param("id") Long id, @Param("updateTime") Timestamp updateTime);

    default void refreshResume(Long id) {
        updateUpdateTime(id, new Timestamp(System.currentTimeMillis()));
    }
}