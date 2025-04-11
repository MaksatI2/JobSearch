package org.example.JobSearch.service;

public interface ResponseService {
    void respondToVacancy(Long resumeId, Long vacancyId);
    int getResponsesCountByApplicant(String email);
}
