package org.example.JobSearch.service;

import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;

import java.util.List;

public interface VacancyService {
    List<VacancyDTO> getVacanciesByEmployer(Long employerId);

    List<VacancyDTO> getVacanciesByCategory(Long categoryId);

    void createVacancy(VacancyDTO vacancyDto);

    void updateVacancy(Long vacancyId, EditVacancyDTO editvacancyDto);

    void deleteVacancy(Long vacancyId);

    List<VacancyDTO> getAllVacancies();

    List<VacancyDTO> getRespApplToVacancy(Long applicantId);

}
