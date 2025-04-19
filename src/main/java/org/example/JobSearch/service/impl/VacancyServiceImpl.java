package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.exceptions.CategoryNotFoundException;
import org.example.JobSearch.exceptions.CreateVacancyException;
import org.example.JobSearch.exceptions.EditVacancyException;
import org.example.JobSearch.exceptions.VacancyNotFoundException;
import org.example.JobSearch.model.*;
import org.example.JobSearch.repository.*;
import org.example.JobSearch.service.VacancyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

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

    @Transactional(readOnly = true)
    @Override
    public Page<VacancyDTO> getVacanciesByEmployer(Long employerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vacancy> vacanciesPage = vacancyRepository.findByAuthorId(employerId, pageable);
        return vacanciesPage.map(this::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
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
    public Page<VacancyDTO> getAllVacanciesSorted(String sort, Pageable pageable) {
        return vacancyRepository.findAllActiveSorted(sort, pageable)
                .map(this::toDTO);
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
                .authorName(vacancy.getAuthor().getName())
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

    @Override
    public void validateVacancyData(CreateVacancyDTO createVacancyDto, BindingResult bindingResult) {
        if (createVacancyDto.getSalary() != null) {
            try {
                Integer.parseInt(createVacancyDto.getSalary().toString());
            } catch (NumberFormatException e) {
                throw new CreateVacancyException("salary", "Зарплата должна содержать только цифры");
            }

            if (createVacancyDto.getSalary() < 0) {
                throw new CreateVacancyException("salary", "Зарплата не может быть отрицательной");
            }
        }

        if (createVacancyDto.getExpFrom() != null) {
            try {
                Integer.parseInt(createVacancyDto.getExpFrom().toString());
            } catch (NumberFormatException e) {
                throw new CreateVacancyException("expFrom", "Опыт 'от' должен содержать только цифры");
            }

            if (createVacancyDto.getExpFrom() < 0) {
                throw new CreateVacancyException("expFrom", "Опыт 'от' не может быть отрицательным");
            }
        }

        if (createVacancyDto.getExpTo() != null) {
            try {
                Integer.parseInt(createVacancyDto.getExpTo().toString());
            } catch (NumberFormatException e) {
                throw new CreateVacancyException("expTo", "Опыт 'до' должен содержать только цифры");
            }

            if (createVacancyDto.getExpTo() < 0) {
                throw new CreateVacancyException("expTo", "Опыт 'до' не может быть отрицательным");
            }
        }

        if (createVacancyDto.getExpFrom() != null && createVacancyDto.getExpTo() != null) {
            if (createVacancyDto.getExpFrom() > createVacancyDto.getExpTo()) {
                throw new CreateVacancyException("expFrom", "Начальный опыт не может быть больше конечного");
            }
        }
    }

    @Override
    public void validateEditVacancyData(EditVacancyDTO editVacancyDto, BindingResult bindingResult) {
        if (editVacancyDto.getSalary() != null) {
            try {
                Integer.parseInt(editVacancyDto.getSalary().toString());
            } catch (NumberFormatException e) {
                throw new EditVacancyException("salary", "Зарплата должна содержать только цифры");
            }

            if (editVacancyDto.getSalary() < 0) {
                throw new EditVacancyException("salary", "Зарплата не может быть отрицательной");
            }
        }

        if (editVacancyDto.getExpFrom() != null) {
            try {
                Integer.parseInt(editVacancyDto.getExpFrom().toString());
            } catch (NumberFormatException e) {
                throw new EditVacancyException("expFrom", "Опыт 'от' должен содержать только цифры");
            }

            if (editVacancyDto.getExpFrom() < 0) {
                throw new EditVacancyException("expFrom", "Опыт 'от' не может быть отрицательным");
            }
        }

        if (editVacancyDto.getExpTo() != null) {
            try {
                Integer.parseInt(editVacancyDto.getExpTo().toString());
            } catch (NumberFormatException e) {
                throw new EditVacancyException("expTo", "Опыт 'до' должен содержать только цифры");
            }

            if (editVacancyDto.getExpTo() < 0) {
                throw new EditVacancyException("expTo", "Опыт 'до' не может быть отрицательным");
            }
        }

        if (editVacancyDto.getExpFrom() != null && editVacancyDto.getExpTo() != null) {
            if (editVacancyDto.getExpFrom() > editVacancyDto.getExpTo()) {
                throw new EditVacancyException("expFrom", "Начальный опыт не может быть больше конечного");
            }
        }
    }
}