package org.example.JobSearch.repository;

import org.example.JobSearch.model.RespondedApplicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RespondedApplicantRepository extends JpaRepository<RespondedApplicant, Long> {

    boolean existsByResumeIdAndVacancyId(Long resumeId, Long vacancyId);

    @Query("""
        SELECT COUNT(ra) 
        FROM RespondedApplicant ra
        WHERE ra.resume.applicant.id = :applicantId
    """)
    int countByApplicantId(Long applicantId);

    @Query("SELECT COUNT(ra) FROM RespondedApplicant ra WHERE ra.vacancy.id = :vacancyId")
    int countByVacancyId(@Param("vacancyId") Long vacancyId);

    Page<RespondedApplicant> findByVacancyId(Long vacancyId, Pageable pageable);
}
