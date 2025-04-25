package org.example.JobSearch.service;

import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.model.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

public interface VacancyService {
    Page<VacancyDTO> getVacanciesByEmployer(Long employerId, int page, int size);

    void createVacancy(CreateVacancyDTO createvacancyDto, Long employerId);
    void updateVacancy(Long vacancyId, EditVacancyDTO editvacancyDto);
    void deleteVacancy(Long vacancyId);
    void refreshVacancy(Long vacancyId);

    Page<VacancyDTO> getAllVacanciesSorted(String sort, Pageable pageable);

    Vacancy getVacancyEntityById(Long id);

    VacancyDTO getVacancyById(Long id);

    @Transactional(readOnly = true)
    Page<VacancyDTO> getVacanciesWithResponsesByAuthorId(Long authorId, Pageable pageable);

    EditVacancyDTO convertToEditDTO(VacancyDTO dto);

    void validateVacancyData(CreateVacancyDTO  createvacancyDto, BindingResult bindingResult);

    void validateEditVacancyData(EditVacancyDTO  editVacancyDTO, BindingResult bindingResult);
}
