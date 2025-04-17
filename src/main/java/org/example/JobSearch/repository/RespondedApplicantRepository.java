package org.example.JobSearch.repository;

import org.example.JobSearch.model.RespondedApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}
