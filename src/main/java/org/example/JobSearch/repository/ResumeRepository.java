package org.example.JobSearch.repository;

import org.example.JobSearch.model.Resume;
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

    Page<Resume> findByIsActiveTrue(Pageable pageable);

    Page<Resume> findByApplicantId(Long applicantId, Pageable pageable);

    boolean existsById(Long id);

    @Modifying
    @Query("UPDATE Resume r SET r.updateTime = :updateTime WHERE r.id = :id")
    void updateUpdateTime(@Param("id") Long id, @Param("updateTime") Timestamp updateTime);

    default void refreshResume(Long id) {
        updateUpdateTime(id, new Timestamp(System.currentTimeMillis()));
    }
}