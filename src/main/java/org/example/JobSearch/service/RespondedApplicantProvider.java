package org.example.JobSearch.service;

import org.example.JobSearch.model.RespondedApplicant;

import java.util.List;

public interface RespondedApplicantProvider {
    RespondedApplicant getById(Long id);

    List<RespondedApplicant> getByApplicantId(Long applicantId);

    List<RespondedApplicant> getByEmployerId(Long employerId);
}
