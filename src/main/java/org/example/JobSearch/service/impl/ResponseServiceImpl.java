package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.RespondedApplicantDao;
import org.example.JobSearch.dao.UserDao;
import org.example.JobSearch.model.RespondedApplicant;
import org.example.JobSearch.service.ResponseService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ResponseServiceImpl implements ResponseService {

    private final RespondedApplicantDao respondedApplicantDao;
    private final UserDao userDao;

    @Override
    public void respondToVacancy(Long resumeId, Long vacancyId) {
        if (respondedApplicantDao.existsByResumeIdAndVacancyId(resumeId, vacancyId)) {
            throw new IllegalStateException("Уже откликались на эту вакансию.");
        }

        RespondedApplicant response = new RespondedApplicant();
        response.setResumeId(resumeId);
        response.setVacancyId(vacancyId);
        response.setConfirmation(false);
        respondedApplicantDao.save(response);
    }

    @Override
    public int getResponsesCountByApplicant(String email) {
        Long applicantId = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"))
                .getId();

        return respondedApplicantDao.countByApplicantId(applicantId);
    }
}
