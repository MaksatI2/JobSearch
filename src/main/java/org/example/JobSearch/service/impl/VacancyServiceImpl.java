package org.example.JobSearch.service.impl;

import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    @Override
    public List<VacancyDTO> getVacanciesByCategory(Integer categoryId) {
        //TODO: Сделать логику для вывода всех вакансии по категории
        return List.of();
    }

    @Override
    public void createVacancy(VacancyDTO vacancyDto) {
        //TODO: Сделать логику для создания вакансии
    }

    @Override
    public void updateVacancy(Integer vacancyId, VacancyDTO vacancyDto) {
        //TODO: Сделать логику для обновления вакансии
    }

    @Override
    public void deleteVacancy(Integer vacancyId) {
        //TODO: Сделать логику для удаления вакансии
    }

    @Override
    public void getApplicantsVacancy(Integer vacancyId) {
        //TODO: Сделать логику для вывода всех вакансии по категории
    }

    @Override
    public List<VacancyDTO> getAllVacancies() {
        //TODO: Сделать логику для вывода всех вакансий
        return List.of();
    }
}
