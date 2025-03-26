package org.example.JobSearch.service.impl;

import org.example.JobSearch.dao.UserDao;
import org.example.JobSearch.dao.VacancyDao;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.exceptions.InvalidUserDataException;
import org.example.JobSearch.exceptions.VacancyNotFoundException;
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
    private final UserDao userDao;

    @Override
    public List<VacancyDTO> getVacanciesByCategory(Long categoryId) {
        List<VacancyDTO> vacancies = vacancyDao.getVacanciesByCategory(categoryId).stream().map(this::toDTO).collect(Collectors.toList());
        if (vacancies.isEmpty()) {
            throw new VacancyNotFoundException("No vacancies found for category ID: " + categoryId);
        }
        return vacancies;
    }

    @Override
    public void createVacancy(VacancyDTO vacancyDto) {
        if (!userDao.isUserEmployer(vacancyDto.getAuthorId())) {
            throw new InvalidUserDataException("Only employers can create vacancies");
        }
        vacancyDao.createVacancy(vacancyDto);
    }

    @Override
    public void updateVacancy(Long vacancyId, VacancyDTO vacancyDto) {
        Vacancy existingVacancy = vacancyDao.getVacancyById(vacancyId);
        if (existingVacancy == null) {
            throw new VacancyNotFoundException("Vacancy not found with ID: " + vacancyId);
        }

        if (!userDao.isUserEmployer(vacancyDto.getAuthorId())) {
            throw new InvalidUserDataException("Only employers can update vacancies");
        }

        vacancyDao.updateVacancy(vacancyId, vacancyDto);
    }

    @Override
    public void deleteVacancy(Long vacancyId) {
        if (vacancyDao.getVacancyById(vacancyId) == null) {
            throw new VacancyNotFoundException("Vacancy not found with ID: " + vacancyId);
        }
        vacancyDao.deleteVacancy(vacancyId);
    }

    @Override
    public List<VacancyDTO> getAllVacancies() {
        List<VacancyDTO> vacancies = vacancyDao.getAllVacancies().stream().map(this::toDTO).collect(Collectors.toList());
        if (vacancies.isEmpty()) {
            throw new VacancyNotFoundException("No vacancies found");
        }
        return vacancies;
    }

    @Override
    public List<VacancyDTO> getRespApplToVacancy(Long applicantId) {
        List<VacancyDTO> vacancies = vacancyDao.getRespondedVacancies(applicantId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        if (vacancies.isEmpty()) {
            throw new VacancyNotFoundException("No responded vacancies found for applicant ID: " + applicantId);
        }
        return vacancies;
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