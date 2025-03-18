package org.example.JobSearch.service;

import org.example.JobSearch.dto.RespondedApplicantDTO;

public interface ApplicationService {

    void respondToVacancy(Long vacancyId, RespondedApplicantDTO respondedApplicantDto);
}
