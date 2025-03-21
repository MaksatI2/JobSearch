package org.example.JobSearch.service;

import org.example.JobSearch.dto.VacancyDTO;

import java.util.List;

public interface VacancyService {
    List<VacancyDTO> getVacanciesByCategory(Long categoryId);

    void createVacancy(VacancyDTO vacancyDto);

    void updateVacancy(Long vacancyId, VacancyDTO vacancyDto);

    void deleteVacancy(Long vacancyId);

    List<VacancyDTO> getAllVacancies();

    List<VacancyDTO> getRespApplToVacancy(Long applicantId);

}
