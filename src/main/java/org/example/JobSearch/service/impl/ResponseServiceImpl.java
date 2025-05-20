package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.MessageDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.model.RespondedApplicant;
import org.example.JobSearch.repository.RespondedApplicantRepository;
import org.example.JobSearch.service.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private final RespondedApplicantRepository respondedApplicantRepository;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final MessageService messageService;

    @Override
    @Transactional
    public void respondToVacancy(Long resumeId, Long vacancyId) {
        if (respondedApplicantRepository.existsByResumeIdAndVacancyId(resumeId, vacancyId)) {
            throw new IllegalStateException(getMessage("response.already"));
        }

        RespondedApplicant response = new RespondedApplicant();
        response.setResume(resumeService.getResumeEntityById(resumeId));
        response.setVacancy(vacancyService.getVacancyEntityById(vacancyId));
        response.setConfirmation(null);
        respondedApplicantRepository.save(response);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("Здравствуйте, меня интересует ваша вакансия...");
        messageDTO.setSendTime(new Timestamp(Instant.now().toEpochMilli()));
        messageDTO.setSenderType(AccountType.APPLICANT);
        messageDTO.setRespondedApplicantId(response.getId());
        messageDTO.setRead(false);

        messageService.saveMessage(messageDTO);
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

    @Transactional(readOnly = true)
    @Override
    public Page<VacancyDTO> getVacanciesByResumeId(Long resumeId, Pageable pageable) {
        return respondedApplicantRepository.findByResumeId(resumeId, pageable)
                .map(response -> {
                    VacancyDTO vacancy = vacancyService.getVacancyById(response.getVacancy().getId());
                    return vacancy;
                });
    }

    @Transactional(readOnly = true)
    @Override
    public int getResponsesCountByResume(Long resumeId) {
        return respondedApplicantRepository.countByResumeId(resumeId);
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

}