package org.example.JobSearch.service;

import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ResponseService {
    @Transactional(readOnly = true)
    int getResponsesCountByEmployer(String email);
    @Transactional(readOnly = true)
    int getResponsesCountByResume(Long resumeId);
    int getResponsesCountByVacancy(Long vacancyId);
    int getResponsesCountByApplicant(String email);

    @Transactional(readOnly = true)
    Page<VacancyDTO> getVacanciesByResumeId(Long resumeId, Pageable pageable);
    Page<ResumeDTO> getResumesByVacancyId(Long vacancyId, Pageable pageable);

    @Transactional
    void markApplicantResponsesAsViewed(Long applicantId);
    @Transactional
    void markEmployerResponsesAsViewed(Long employerId);
    void respondToVacancy(Long resumeId, Long vacancyId);
}
