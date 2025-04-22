package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.RespondedApplicant;
import org.example.JobSearch.repository.RespondedApplicantRepository;
import org.example.JobSearch.service.ResumeService;
import org.example.JobSearch.service.ResponseService;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.service.VacancyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private final RespondedApplicantRepository respondedApplicantRepository;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final UserService userService;

    @Override
    @Transactional
    public void respondToVacancy(Long resumeId, Long vacancyId) {
        if (respondedApplicantRepository.existsByResumeIdAndVacancyId(resumeId, vacancyId)) {
            throw new IllegalStateException("Уже откликались на эту вакансию.");
        }

        RespondedApplicant response = new RespondedApplicant();
        response.setResume(resumeService.getResumeEntityById(resumeId));
        response.setVacancy(vacancyService.getVacancyEntityById(vacancyId));
        response.setConfirmation(false);
        respondedApplicantRepository.save(response);
    }

    @Override
    @Transactional(readOnly = true)
    public int getResponsesCountByApplicant(String email) {
        Long applicantId = userService.getUserId(email);
        return respondedApplicantRepository.countByApplicantId(applicantId);
    }

    @Transactional(readOnly = true)
    @Override
    public int getResponsesCountByVacancy(Long vacancyId) {
        return respondedApplicantRepository.countByVacancyId(vacancyId);
    }
}