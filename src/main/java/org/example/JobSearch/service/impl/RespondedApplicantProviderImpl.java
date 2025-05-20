package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.RespondedApplicant;
import org.example.JobSearch.repository.RespondedApplicantRepository;
import org.example.JobSearch.service.RespondedApplicantProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RespondedApplicantProviderImpl implements RespondedApplicantProvider {
    private final RespondedApplicantRepository repository;

    @Override
    public RespondedApplicant getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Responded applicant not found"));
    }

    @Override
    public List<RespondedApplicant> getByApplicantId(Long applicantId) {
        return repository.findByResumeApplicantId(applicantId);
    }

    @Override
    public List<RespondedApplicant> getByEmployerId(Long employerId) {
        return repository.findByVacancyAuthorId(employerId);
    }
}
