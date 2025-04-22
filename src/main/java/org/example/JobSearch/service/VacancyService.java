package org.example.JobSearch.service;

import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.model.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface VacancyService {
    Page<VacancyDTO> getVacanciesByEmployer(Long employerId, int page, int size);

//      оставил на потом при поиске по категории
//    List<VacancyDTO> getVacanciesByCategory(Long categoryId);

    void createVacancy(CreateVacancyDTO createvacancyDto, Long employerId);
    void updateVacancy(Long vacancyId, EditVacancyDTO editvacancyDto);
    void deleteVacancy(Long vacancyId);
    void refreshVacancy(Long vacancyId);

    Page<VacancyDTO> getAllVacanciesSorted(String sort, Pageable pageable);

//    List<VacancyDTO> getRespApplToVacancy(Long applicantId);

    Vacancy getVacancyEntityById(Long id);

    VacancyDTO getVacancyById(Long id);

    EditVacancyDTO convertToEditDTO(VacancyDTO dto);

    void validateVacancyData(CreateVacancyDTO  createvacancyDto, BindingResult bindingResult);

    void validateEditVacancyData(EditVacancyDTO  editVacancyDTO, BindingResult bindingResult);
}
