package org.example.JobSearch.service;

import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface VacancyService {
    List<VacancyDTO> getVacanciesByEmployer(Long employerId);

    List<VacancyDTO> getVacanciesByCategory(Long categoryId);

    void createVacancy(CreateVacancyDTO createvacancyDto, Long employerId);

    void updateVacancy(Long vacancyId, EditVacancyDTO editvacancyDto);

    void deleteVacancy(Long vacancyId);

    List<VacancyDTO> getAllVacancies();

    List<VacancyDTO> getRespApplToVacancy(Long applicantId);

    VacancyDTO getVacancyById(Long id);

    EditVacancyDTO convertToEditDTO(VacancyDTO dto);

    void refreshVacancy(Long vacancyId);

    void validateVacancyData(CreateVacancyDTO  createvacancyDto, BindingResult bindingResult);

    void validateEditVacancyData(EditVacancyDTO  editVacancyDTO, BindingResult bindingResult);
}
