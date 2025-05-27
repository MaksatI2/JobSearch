package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.exceptions.CreateVacancyException;
import org.example.JobSearch.exceptions.EditVacancyException;
import org.example.JobSearch.exceptions.VacancyNotFoundException;
import org.example.JobSearch.model.*;
import org.example.JobSearch.repository.VacancyRepository;
import org.example.JobSearch.service.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final MessageSource messageSource;
    private final RegionService regionService;
    private final WorkScheduleService workScheduleService;
    private final EmploymentTypeService employmentTypeService;

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
                .orElseThrow(() -> new IllegalArgumentException(getMessage("vacancy.not.found")));
    }

    @Override
    @Transactional
    public void createVacancy(CreateVacancyDTO createVacancyDto, Long employerId) {
        log.info("Создание новой вакансии работодателем ID: {}", employerId);

        User employer = userService.getUserId(employerId);

        if (!employer.getAccountType().equals(AccountType.EMPLOYER)) {
            log.error("Попытка создания вакансии пользователем, не являющимся работодателем: {}", employerId);
            throw new AccessDeniedException(getMessage("vacancy.creation.forbidden"));
        }

        Category category = categoryService.getCategoryById(createVacancyDto.getCategoryId());
        Optional<Region> region = regionService.findById(createVacancyDto.getRegionId());
        List<WorkSchedule> workSchedules = workScheduleService.findAllById(createVacancyDto.getWorkScheduleIds());
        List<EmploymentType> employmentTypes = employmentTypeService.findAllById(createVacancyDto.getEmploymentTypeIds());

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Vacancy vacancy = Vacancy.builder()
                .author(employer)
                .category(category)
                .name(createVacancyDto.getName())
                .description(createVacancyDto.getDescription())
                .responsibilities(createVacancyDto.getResponsibilities())
                .salary(createVacancyDto.getSalary())
                .expFrom(createVacancyDto.getExpFrom())
                .expTo(createVacancyDto.getExpTo())
                .isActive(createVacancyDto.getIsActive())
                .region(region.orElse(null))
                .workSchedules(workSchedules)
                .employmentTypes(employmentTypes)
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
                .orElseThrow(() -> new VacancyNotFoundException(getMessage("vacancy.not.found")));

        if (editVacancyDto.getCategoryId() != null) {
            Category category = categoryService.getCategoryById(editVacancyDto.getCategoryId());
            vacancy.setCategory(category);
        }

        if (editVacancyDto.getRegionId() != null) {
            Optional<Region> region = regionService.findById(editVacancyDto.getRegionId());
            vacancy.setRegion(region.orElse(null));
        }

        if (editVacancyDto.getWorkScheduleIds() != null) {
            if (editVacancyDto.getWorkScheduleIds().isEmpty()) {
                vacancy.setWorkSchedules(new ArrayList<>());
            } else {
                List<WorkSchedule> workSchedules = workScheduleService.findAllById(editVacancyDto.getWorkScheduleIds());
                vacancy.setWorkSchedules(workSchedules);
            }
        }

        if (editVacancyDto.getEmploymentTypeIds() != null) {
            if (editVacancyDto.getEmploymentTypeIds().isEmpty()) {
                vacancy.setEmploymentTypes(new ArrayList<>());
            } else {
                List<EmploymentType> employmentTypes = employmentTypeService.findAllById(editVacancyDto.getEmploymentTypeIds());
                vacancy.setEmploymentTypes(employmentTypes);
            }
        }

        vacancy.setName(editVacancyDto.getName());
        vacancy.setDescription(editVacancyDto.getDescription());
        vacancy.setResponsibilities(editVacancyDto.getResponsibilities());
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
            throw new VacancyNotFoundException(getMessage("vacancy.not.found.id") + " " + vacancyId);
        }
        vacancyRepository.deleteById(vacancyId);
        log.info("Вакансия ID {} успешно удалена", vacancyId);
    }

    @Override
    public Page<VacancyDTO> getAllVacanciesSorted(String sort, Long categoryId, Long regionId, Pageable pageable) {
        return vacancyRepository.findAllActiveSorted(sort, categoryId, regionId, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VacancyDTO> searchVacanciesByName(String query, int limit) {
        String searchQuery = query.trim();
        if (searchQuery.isEmpty()) {
            return Collections.emptyList();
        }

        Pageable pageable = PageRequest.of(0, limit);
        return vacancyRepository.searchByNameIgnoreCase(searchQuery, pageable)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VacancyDTO getVacancyById(Long id) {
        log.info("Поиск вакансии по ID: {}", id);
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new VacancyNotFoundException(getMessage("vacancy.not.found")));
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
        int responsesCount = vacancy.getApplicants().size();
        return VacancyDTO.builder()
                .id(vacancy.getId())
                .authorId(vacancy.getAuthor().getId())
                .authorName(vacancy.getAuthor().getName())
                .categoryId(vacancy.getCategory().getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .responsibilities(vacancy.getResponsibilities())
                .salary(vacancy.getSalary())
                .expFrom(vacancy.getExpFrom())
                .expTo(vacancy.getExpTo())
                .isActive(vacancy.getIsActive())
                .regionId(vacancy.getRegion() != null ? vacancy.getRegion().getId() : null)
                .workScheduleIds(vacancy.getWorkSchedules().stream()
                        .map(WorkSchedule::getId)
                        .collect(Collectors.toList()))
                .employmentTypeIds(vacancy.getEmploymentTypes().stream()
                        .map(EmploymentType::getId)
                        .collect(Collectors.toList()))
                .createDate(vacancy.getCreatedDate())
                .updateTime(vacancy.getUpdateTime())
                .responsesCount(responsesCount)
                .build();
    }

    @Override
    public EditVacancyDTO convertToEditDTO(VacancyDTO dto) {
        return EditVacancyDTO.builder()
                .categoryId(dto.getCategoryId())
                .name(dto.getName())
                .description(dto.getDescription())
                .responsibilities(dto.getResponsibilities())
                .salary(dto.getSalary())
                .expFrom(dto.getExpFrom())
                .expTo(dto.getExpTo())
                .isActive(dto.getIsActive())
                .regionId(dto.getRegionId())
                .workScheduleIds(dto.getWorkScheduleIds() != null ? dto.getWorkScheduleIds() : new ArrayList<>())
                .employmentTypeIds(dto.getEmploymentTypeIds() != null ? dto.getEmploymentTypeIds() : new ArrayList<>())
                .updateTime(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    @Override
    public void validateVacancyData(CreateVacancyDTO createVacancyDto, BindingResult bindingResult) {
        if (createVacancyDto.getSalary() != null) {
            if (createVacancyDto.getSalary() < 0) {
                throw new CreateVacancyException("salary", getMessage("vacancy.salary.negative"));
            }
        }

        if (createVacancyDto.getExpFrom() != null) {
            if (createVacancyDto.getExpFrom() < 0) {
                throw new CreateVacancyException("expFrom", getMessage("vacancy.expFrom.negative"));
            }
        }

        if (createVacancyDto.getExpTo() != null) {
            if (createVacancyDto.getExpTo() < 0) {
                throw new CreateVacancyException("expTo", getMessage("vacancy.expTo.negative"));
            }
        }

        if (createVacancyDto.getExpFrom() != null && createVacancyDto.getExpTo() != null) {
            if (createVacancyDto.getExpFrom() > createVacancyDto.getExpTo()) {
                throw new CreateVacancyException("expFrom", getMessage("vacancy.exp.range.invalid"));
            }
        }
    }

    @Override
    public void validateEditVacancyData(EditVacancyDTO editVacancyDto, BindingResult bindingResult) {
        if (editVacancyDto.getSalary() != null) {
            if (editVacancyDto.getSalary() < 0) {
                throw new EditVacancyException("salary", getMessage("vacancy.salary.negative"));
            }
        }

        if (editVacancyDto.getExpFrom() != null) {
            if (editVacancyDto.getExpFrom() < 0) {
                throw new EditVacancyException("expFrom", getMessage("vacancy.expFrom.negative"));
            }
        }

        if (editVacancyDto.getExpTo() != null) {
            if (editVacancyDto.getExpTo() < 0) {
                throw new EditVacancyException("expTo", getMessage("vacancy.expTo.negative"));
            }
        }

        if (editVacancyDto.getExpFrom() != null && editVacancyDto.getExpTo() != null) {
            if (editVacancyDto.getExpFrom() > editVacancyDto.getExpTo()) {
                throw new EditVacancyException("expFrom", getMessage("vacancy.exp.range.invalid"));
            }
        }
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}