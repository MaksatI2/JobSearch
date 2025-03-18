package org.example.JobSearch.service;

import org.example.JobSearch.dto.RespondedApplicantDTO;
import org.example.JobSearch.dto.VacancyDTO;

import java.util.List;

public interface VacancyService {
    List<VacancyDTO> getVacanciesByCategory(Integer categoryId);

    void createVacancy(VacancyDTO vacancyDto);

    void updateVacancy(Integer vacancyId, VacancyDTO vacancyDto);

    void deleteVacancy(Integer vacancyId);

    List<VacancyDTO> getAllVacancies();

    void getApplicantsVacancy(Integer vacancyId);
}
