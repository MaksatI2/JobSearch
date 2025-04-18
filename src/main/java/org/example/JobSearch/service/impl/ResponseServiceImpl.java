package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.RespondedApplicant;
import org.example.JobSearch.model.Resume;
import org.example.JobSearch.model.Vacancy;
import org.example.JobSearch.repository.RespondedApplicantRepository;
import org.example.JobSearch.repository.ResumeRepository;
import org.example.JobSearch.repository.UserRepository;
import org.example.JobSearch.repository.VacancyRepository;
import org.example.JobSearch.service.ResponseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private final RespondedApplicantRepository respondedApplicantRepository;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final VacancyRepository vacancyRepository;

    @Override
    @Transactional
    public void respondToVacancy(Long resumeId, Long vacancyId) {
        if (respondedApplicantRepository.existsByResumeIdAndVacancyId(resumeId, vacancyId)) {
            throw new IllegalStateException("Уже откликались на эту вакансию.");
        }

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Резюме не найдено"));
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new IllegalArgumentException("Вакансия не найдена"));

        RespondedApplicant response = new RespondedApplicant();
        response.setResume(resume);
        response.setVacancy(vacancy);
        response.setConfirmation(false);
        respondedApplicantRepository.save(response);
    }

    @Override
    @Transactional(readOnly = true)
    public int getResponsesCountByApplicant(String email) {
        Long applicantId = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"))
                .getId();

        return respondedApplicantRepository.countByApplicantId(applicantId);
    }
}