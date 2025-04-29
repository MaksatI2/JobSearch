package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.model.RespondedApplicant;
import org.example.JobSearch.repository.RespondedApplicantRepository;
import org.example.JobSearch.service.ResumeService;
import org.example.JobSearch.service.ResponseService;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.service.VacancyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        response.setConfirmation(null);
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
    public int getResponsesCountByEmployer(String email) {
        Long employerId = userService.getUserId(email);
        return respondedApplicantRepository.countByEmployerId(employerId);
    }

    @Transactional(readOnly = true)
    @Override
    public int getResponsesCountByVacancy(Long vacancyId) {
        return respondedApplicantRepository.countByVacancyId(vacancyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResumeDTO> getResumesByVacancyId(Long vacancyId, Pageable pageable) {
        return respondedApplicantRepository.findByVacancyId(vacancyId, pageable)
                .map(response -> {
                    ResumeDTO resume = resumeService.getResumeById(response.getResume().getId());
                    String avatarUrl = "/api/users/" + resume.getApplicantId() + "/avatar";
                    resume.setApplicantAvatar(avatarUrl);
                    return resume;
                });
    }

    @Override
    @Transactional
    public void markApplicantResponsesAsViewed(Long applicantId) {
        List<RespondedApplicant> responses = respondedApplicantRepository
                .findByResumeApplicantIdAndViewedFalse(applicantId);
        responses.forEach(response -> response.setViewed(true));
        respondedApplicantRepository.saveAll(responses);
    }

    @Override
    @Transactional
    public void markEmployerResponsesAsViewed(Long employerId) {
        List<RespondedApplicant> responses = respondedApplicantRepository
                .findByVacancyAuthorIdAndConfirmationNull(employerId);
        responses.forEach(response -> response.setConfirmation(false));
        respondedApplicantRepository.saveAll(responses);
    }

}