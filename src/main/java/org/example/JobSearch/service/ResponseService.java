package org.example.JobSearch.service;

import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ResponseService {
    void respondToVacancy(Long resumeId, Long vacancyId);

    int getResponsesCountByApplicant(String email);

    Page<ResumeDTO> getResumesByVacancyId(Long vacancyId, Pageable pageable);

    @Transactional(readOnly = true)
    int getResponsesCountByEmployer(String email);

    int getResponsesCountByVacancy(Long vacancyId);

    @Transactional
    void markApplicantResponsesAsViewed(Long applicantId);

    @Transactional
    void markEmployerResponsesAsViewed(Long employerId);

    @Transactional(readOnly = true)
    Page<VacancyDTO> getVacanciesByResumeId(Long resumeId, Pageable pageable);

    @Transactional(readOnly = true)
    int getResponsesCountByResume(Long resumeId);
}
