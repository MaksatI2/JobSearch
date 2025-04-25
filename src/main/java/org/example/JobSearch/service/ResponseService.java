package org.example.JobSearch.service;

import org.example.JobSearch.dto.ResumeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResponseService {
    void respondToVacancy(Long resumeId, Long vacancyId);

    int getResponsesCountByApplicant(String email);

    Page<ResumeDTO> getResumesByVacancyId(Long vacancyId, Pageable pageable);

    int getResponsesCountByVacancy(Long vacancyId);
}
