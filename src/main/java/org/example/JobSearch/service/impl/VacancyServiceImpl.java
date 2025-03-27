package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.CategoryDao;
import org.example.JobSearch.dao.UserDao;
import org.example.JobSearch.dao.VacancyDao;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.exceptions.VacancyNotFoundException;
import org.example.JobSearch.model.Vacancy;
import org.example.JobSearch.service.VacancyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyDao vacancyDao;
    private final UserDao userDao;
    private final CategoryDao categoryDao;

    @Override
    public List<VacancyDTO> getVacanciesByCategory(Long categoryId) {
        List<VacancyDTO> vacancies = vacancyDao.getVacanciesByCategory(categoryId)
                .stream().map(this::toDTO).collect(Collectors.toList());
        if (vacancies.isEmpty()) {
            throw new VacancyNotFoundException("Вакансий по категории ID не найдено: " + categoryId);
        }
        return vacancies;
    }

    @Override
    public void createVacancy(VacancyDTO vacancyDto) {
        Long authorId = parseAndValidateId(vacancyDto.getAuthorId(), "ID автора");
        Long categoryId = parseAndValidateId(vacancyDto.getCategoryId(), "ID категории");

        if (!userDao.isUserEmployer(authorId)) {
            throw new VacancyNotFoundException("Только работодатели могут создавать вакансии");
        }

        if (!categoryDao.existsById(categoryId)) {
            throw new VacancyNotFoundException("Категория с ID: " + categoryId + " не существует");
        }

        vacancyDto.setAuthorId(authorId.toString());
        vacancyDto.setCategoryId(categoryId.toString());

        validateVacancy(vacancyDto);
        vacancyDao.createVacancy(vacancyDto);
    }

    @Override
    public void updateVacancy(Long vacancyId, VacancyDTO vacancyDto) {
        Vacancy existingVacancy = vacancyDao.getVacancyById(vacancyId);
        if (existingVacancy == null) {
            throw new VacancyNotFoundException("Вакансия с ID: " + vacancyId + " не найдена");
        }

        Long authorId = parseAndValidateId(vacancyDto.getAuthorId(), "ID автора");
        Long categoryId = parseAndValidateId(vacancyDto.getCategoryId(), "ID категории");

        if (!userDao.isUserEmployer(authorId)) {
            throw new VacancyNotFoundException("Только работодатели могут обновлять вакансии");
        }

        if (!categoryDao.existsById(categoryId)) {
            throw new VacancyNotFoundException("Категория с ID: " + categoryId + " не существует");
        }

        if (vacancyDto.getExpFrom() > vacancyDto.getExpTo()) {
            throw new IllegalArgumentException("Минимальный опыт не может быть больше максимального");
        }

        validateVacancy(vacancyDto);
        vacancyDao.updateVacancy(vacancyId, vacancyDto);
    }

    @Override
    public void deleteVacancy(Long vacancyId) {
        if (vacancyDao.getVacancyById(vacancyId) == null) {
            throw new VacancyNotFoundException("Вакансия не найдена по ID: " + vacancyId);
        }
        vacancyDao.deleteVacancy(vacancyId);
    }

    @Override
    public List<VacancyDTO> getAllVacancies() {
        List<VacancyDTO> vacancies = vacancyDao.getAllVacancies().stream().map(this::toDTO).collect(Collectors.toList());
        if (vacancies.isEmpty()) {
            throw new VacancyNotFoundException("Вакансий не найдено");
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
            throw new VacancyNotFoundException("Не найдено ни одной вакансии, на которую был бы дан ответ по ID: " + applicantId);
        }
        return vacancies;
    }

    private VacancyDTO toDTO(Vacancy vacancy) {
        return VacancyDTO.builder()
                .id(vacancy.getId())
                .authorId(String.valueOf(vacancy.getAuthorId()))
                .categoryId(String.valueOf(vacancy.getCategoryId()))
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

    private void validateVacancy(VacancyDTO vacancyDto) {
        if (!vacancyDto.getName().matches("^[a-zA-Zа-яА-ЯёЁ\\s]+$") || vacancyDto.getName().length() < 2) {
            throw new IllegalArgumentException("Название должно содержать только буквы и быть не короче 2 символов");
        }
        if (vacancyDto.getDescription().length() < 10) {
            throw new IllegalArgumentException("Описание вакансии должно содержать не менее 10 символов");
        }
        if (vacancyDto.getSalary() < 0) {
            throw new IllegalArgumentException("Зарплата не может быть отрицательной");
        }
        if (vacancyDto.getExpFrom() < 0) {
            throw new IllegalArgumentException("Минимальный опыт не может быть отрицательным");
        }
        if (vacancyDto.getExpTo() < 0 || vacancyDto.getExpTo() > 50) {
            throw new IllegalArgumentException("Максимальный опыт не может быть отрицательным и не должен превышать 50 лет");
        }
    }

    private Long parseAndValidateId(String id, String fieldName) {
        try {
            Long parsedId = Long.parseLong(id);
            if (parsedId <= 0) {
                throw new IllegalArgumentException(fieldName + " должен быть положительным числом");
            }
            return parsedId;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " должен содержать только цифры");
        }
    }
}
