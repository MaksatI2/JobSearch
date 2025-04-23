package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.exceptions.CreateVacancyException;
import org.example.JobSearch.exceptions.EditVacancyException;
import org.example.JobSearch.exceptions.VacancyNotFoundException;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.model.Category;
import org.example.JobSearch.model.User;
import org.example.JobSearch.model.Vacancy;
import org.example.JobSearch.repository.VacancyRepository;
import org.example.JobSearch.service.CategoryService;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.service.VacancyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    @Override
    public Page<VacancyDTO> getVacanciesByEmployer(Long employerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vacancy> vacanciesPage = vacancyRepository.findByAuthorId(employerId, pageable);
        return vacanciesPage.map(this::toDTO);
    }

    @Override
    public Vacancy getVacancyEntityById(Long id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Вакансия не найдена"));
    }

    @Override
    @Transactional
    public void createVacancy(CreateVacancyDTO createVacancyDto, Long employerId) {
        log.info("Создание новой вакансии работодателем ID: {}", employerId);

        User employer = userService.getUserId(employerId);

        if (!employer.getAccountType().equals(AccountType.EMPLOYER)) {
            log.error("Попытка создания вакансии пользователем, не являющимся работодателем: {}", employerId);
            throw new AccessDeniedException("Только работодатели могут создавать вакансии");
        }

        Category category = categoryService.getCategoryById(createVacancyDto.getCategoryId());

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
            Category category = categoryService.getCategoryById(editVacancyDto.getCategoryId());
            vacancy.setCategory(category);
        }

        vacancy.setName(editVacancyDto.getName());
        vacancy.setDescription(editVacancyDto.getDescription());
        vacancy.setSalary(editVacancyDto.getSalary());
        vacancy.setExpFrom(editVacancyDto.getExpFrom());
        vacancy.setExpTo(editVacancyDto.getExpTo());
        vacancy.setIsActive(editVacancyDto.getIsActive());
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

    @Transactional(readOnly = true)
    @Override
    public Page<VacancyDTO> getVacanciesWithResponsesByAuthorId(Long authorId, Pageable pageable) {
        Page<Vacancy> vacanciesPage = vacancyRepository.findVacanciesWithResponsesByAuthorId(authorId, pageable);
        return vacanciesPage.map(this::toDTO);
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
            if (createVacancyDto.getSalary() < 0) {
                throw new CreateVacancyException("salary", "Зарплата не может быть отрицательной");
            }
        }

        if (createVacancyDto.getExpFrom() != null) {
            if (createVacancyDto.getExpFrom() < 0) {
                throw new CreateVacancyException("expFrom", "Опыт 'от' не может быть отрицательным");
            }
        }

        if (createVacancyDto.getExpTo() != null) {
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
            if (editVacancyDto.getSalary() < 0) {
                throw new EditVacancyException("salary", "Зарплата не может быть отрицательной");
            }
        }

        if (editVacancyDto.getExpFrom() != null) {
            if (editVacancyDto.getExpFrom() < 0) {
                throw new EditVacancyException("expFrom", "Опыт 'от' не может быть отрицательным");
            }
        }

        if (editVacancyDto.getExpTo() != null) {
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