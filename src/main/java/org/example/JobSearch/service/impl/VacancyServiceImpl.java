package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.exceptions.CategoryNotFoundException;
import org.example.JobSearch.exceptions.VacancyNotFoundException;
import org.example.JobSearch.model.*;
import org.example.JobSearch.repository.*;
import org.example.JobSearch.service.VacancyService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VacancyDTO> getVacanciesByEmployer(Long employerId) {
        List<Vacancy> vacancies = vacancyRepository.findByAuthorId(employerId);
        return vacancies.stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacancyDTO> getVacanciesByCategory(Long categoryId) {
        log.info("Поиск вакансий по категории ID: {}", categoryId);
        List<Vacancy> vacancies = vacancyRepository.findActiveByCategoryTree(categoryId);

        if (vacancies.isEmpty()) {
            log.warn("Вакансии по категории ID {} не найдены", categoryId);
            throw new CategoryNotFoundException("Вакансий по категории ID не найдено: " + categoryId);
        }

        log.debug("Найдено {} вакансий по категории ID: {}", vacancies.size(), categoryId);
        return vacancies.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createVacancy(CreateVacancyDTO createVacancyDto, Long employerId) {
        log.info("Создание новой вакансии работодателем ID: {}", employerId);

        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (!employer.getAccountType().equals(AccountType.EMPLOYER)) {
            log.error("Попытка создания вакансии пользователем, не являющимся работодателем: {}", employerId);
            throw new AccessDeniedException("Только работодатели могут создавать вакансии");
        }

        Category category = categoryRepository.findById(createVacancyDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Vacancy vacancy = Vacancy.builder()
                .author(employer)
                .category(category)
                .name(createVacancyDto.getName())
                .description(createVacancyDto.getDescription())
                .salary(createVacancyDto.getSalary())
                .expFrom(createVacancyDto.getExpFrom())
                .expTo(createVacancyDto.getExpTo())
                .isActive(createVacancyDto.getIsActive())
                .createdDate(now)
                .updateTime(now)
                .build();

        vacancyRepository.save(vacancy);
        log.info("Вакансия успешно создана: {}", vacancy.getName());
    }

    @Override
    @Transactional
    public void updateVacancy(Long vacancyId, EditVacancyDTO editVacancyDto) {
        log.info("Обновление вакансии ID: {}", vacancyId);

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new VacancyNotFoundException("Вакансия не найдена"));

        if (editVacancyDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(editVacancyDto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
            vacancy.setCategory(category);
        }

        if (editVacancyDto.getName() != null) {
            vacancy.setName(editVacancyDto.getName());
        }

        if (editVacancyDto.getDescription() != null) {
            vacancy.setDescription(editVacancyDto.getDescription());
        }

        if (editVacancyDto.getSalary() != null) {
            vacancy.setSalary(editVacancyDto.getSalary());
        }

        if (editVacancyDto.getExpFrom() != null) {
            vacancy.setExpFrom(editVacancyDto.getExpFrom());
        }

        if (editVacancyDto.getExpTo() != null) {
            vacancy.setExpTo(editVacancyDto.getExpTo());
        }

        if (editVacancyDto.getIsActive() != null) {
            vacancy.setIsActive(editVacancyDto.getIsActive());
        }

        vacancy.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        vacancyRepository.save(vacancy);
        log.info("Вакансия ID {} успешно обновлена", vacancyId);
    }

    @Override
    @Transactional
    public void deleteVacancy(Long vacancyId) {
        log.info("Удаление вакансии ID: {}", vacancyId);
        if (!vacancyRepository.existsById(vacancyId)) {
            log.error("Вакансия для удаления не найдена ID: {}", vacancyId);
            throw new VacancyNotFoundException("Вакансия не найдена по ID: " + vacancyId);
        }
        vacancyRepository.deleteById(vacancyId);
        log.info("Вакансия ID {} успешно удалена", vacancyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacancyDTO> getAllVacancies() {
        log.info("Получение всех активных вакансий");
        List<Vacancy> vacancies = vacancyRepository.findAllByIsActiveTrue();
        return vacancies.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacancyDTO> getRespApplToVacancy(Long applicantId) {
        log.info("Получение вакансий с откликами от соискателя ID: {}", applicantId);
        List<Vacancy> vacancies = vacancyRepository.findRespondedByApplicantId(applicantId);

        if (vacancies.isEmpty()) {
            log.warn("Не найдено вакансий с откликами от соискателя ID: {}", applicantId);
            throw new VacancyNotFoundException("Не найдено ни одной вакансии, на которую был отклик по ID: " + applicantId);
        }

        log.debug("Найдено {} вакансий с откликами от соискателя ID: {}", vacancies.size(), applicantId);
        return vacancies.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VacancyDTO getVacancyById(Long id) {
        log.info("Поиск вакансии по ID: {}", id);
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new VacancyNotFoundException("Вакансия не найдена"));
        return toDTO(vacancy);
    }

    @Override
    @Transactional
    public void refreshVacancy(Long vacancyId) {
        vacancyRepository.refreshVacancy(vacancyId);
    }

    private VacancyDTO toDTO(Vacancy vacancy) {
        return VacancyDTO.builder()
                .id(vacancy.getId())
                .authorId(vacancy.getAuthor().getId())
                .categoryId(vacancy.getCategory().getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .salary(vacancy.getSalary())
                .expFrom(vacancy.getExpFrom())
                .expTo(vacancy.getExpTo())
                .isActive(vacancy.getIsActive())
                .createDate(vacancy.getCreatedDate())
                .updateTime(vacancy.getUpdateTime())
                .build();
    }

    @Override
    public EditVacancyDTO convertToEditDTO(VacancyDTO dto) {
        return EditVacancyDTO.builder()
                .categoryId(dto.getCategoryId())
                .name(dto.getName())
                .description(dto.getDescription())
                .salary(dto.getSalary())
                .expFrom(dto.getExpFrom())
                .expTo(dto.getExpTo())
                .isActive(dto.getIsActive())
                .updateTime(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}