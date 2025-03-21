package org.example.JobSearch.service.impl;

import org.example.JobSearch.dao.VacancyDao;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.model.Vacancy;
import org.example.JobSearch.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyDao vacancyDao;

    @Override
    public List<VacancyDTO> getVacanciesByCategory(Long categoryId) {
        return vacancyDao.getVacanciesByCategory(categoryId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void createVacancy(VacancyDTO vacancyDto) {
        vacancyDao.createVacancy(vacancyDto);
    }

    @Override
    public void updateVacancy(Long vacancyId, VacancyDTO vacancyDto) {
        vacancyDao.updateVacancy(vacancyId, vacancyDto);
    }

    @Override
    public void deleteVacancy(Long vacancyId) {
        vacancyDao.deleteVacancy(vacancyId);
    }

    @Override
    public List<VacancyDTO> getAllVacancies() {
        return vacancyDao.getAllVacancies().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<VacancyDTO> getRespApplToVacancy(Long applicantId) {
        return vacancyDao.getRespondedVacancies(applicantId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private VacancyDTO toDTO(Vacancy vacancy) {
        return VacancyDTO.builder()
                .id(vacancy.getId())
                .authorId(vacancy.getAuthorId())
                .categoryId(vacancy.getCategoryId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .salary(vacancy.getSalary())
                .expFrom(vacancy.getExpFrom())
                .expTo(vacancy.getExpTo())
                .isActive(vacancy.getIsActive())
                .createdDate(vacancy.getCreatedDate())
                .updateTime(vacancy.getUpdateTime())
                .build();
    }
}
