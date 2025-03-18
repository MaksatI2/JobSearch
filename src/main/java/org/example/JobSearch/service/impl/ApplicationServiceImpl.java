package org.example.JobSearch.service.impl;

import org.example.JobSearch.dto.RespondedApplicantDTO;
import org.example.JobSearch.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    @Override
    public void respondToVacancy(Integer vacancyId, RespondedApplicantDTO respondedApplicantDto) {
        //TODO Сделать логику для отклика на вакансию
    }
}
