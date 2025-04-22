package org.example.JobSearch.service;

import org.springframework.transaction.annotation.Transactional;

public interface ResponseService {
    void respondToVacancy(Long resumeId, Long vacancyId);
    int getResponsesCountByApplicant(String email);

    @Transactional(readOnly = true)
    int getResponsesCountByVacancy(Long vacancyId);
}
