package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dao.CategoryDao;
import org.example.JobSearch.dao.UserDao;
import org.example.JobSearch.dao.VacancyDao;
import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.dto.create.CreateVacancyDTO;
import org.example.JobSearch.exceptions.CategoryNotFoundException;
import org.example.JobSearch.exceptions.VacancyNotFoundException;
import org.example.JobSearch.model.Vacancy;
import org.example.JobSearch.service.VacancyService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyDao vacancyDao;
    private final UserDao userDao;
    private final CategoryDao categoryDao;

    @Override
    public List<VacancyDTO> getVacanciesByEmployer(Long employerId) {
        List<Vacancy> vacancies = vacancyDao.getVacanciesByEmployer(employerId);
        return vacancies.stream().map(this::toDTO).toList();
    }

    @Override
    public List<VacancyDTO> getVacanciesByCategory(Long categoryId) {
        log.info("Поиск вакансий по категории ID: {}", categoryId);
        List<VacancyDTO> vacancies = vacancyDao.getVacanciesByCategory(categoryId)
                .stream().map(this::toDTO).collect(Collectors.toList());

        if (vacancies.isEmpty()) {
            log.warn("Вакансии по категории ID {} не найдены", categoryId);
            throw new CategoryNotFoundException("Вакансий по категории ID не найдено: " + categoryId);
        }

        log.debug("Найдено {} вакансий по категории ID: {}", vacancies.size(), categoryId);
        return vacancies;
    }

    @Override
    public void createVacancy(CreateVacancyDTO createVacancyDto, Long employerId) {
        log.info("Создание новой вакансии работодателем ID: {}", employerId);

        if (!userDao.isUserEmployer(employerId)) {
            log.error("Попытка создания вакансии пользователем, не являющимся работодателем: {}", employerId);
            throw new AccessDeniedException("Только работодатели могут создавать вакансии");
        }

        Long categoryId = createVacancyDto.getCategoryId();

        if (!categoryDao.existsById(categoryId)) {
            log.error("Попытка создания вакансии с несуществующей категорией ID: {}", categoryId);
            throw new IllegalArgumentException("Категория с ID: " + categoryId + " не существует");
        }

        VacancyDTO vacancyDto = VacancyDTO.builder()
                .authorId(employerId)
                .categoryId(categoryId)
                .name(createVacancyDto.getName())
                .description(createVacancyDto.getDescription())
                .salary(createVacancyDto.getSalary())
                .expFrom(createVacancyDto.getExpFrom())
                .expTo(createVacancyDto.getExpTo())
                .isActive(createVacancyDto.getIsActive())
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateTime(new Timestamp(System.currentTimeMillis()))
                .build();

        vacancyDao.createVacancy(vacancyDto);
        log.info("Вакансия успешно создана: {}", vacancyDto.getName());
    }

    @Override
    public void updateVacancy(Long vacancyId, EditVacancyDTO editvacancyDto) {
        log.info("Обновление вакансии ID: {}", vacancyId);
        if (!vacancyDao.existsVacancy(vacancyId)) {
            log.error("Вакансия для обновления не найдена ID: {}", vacancyId);
            throw new VacancyNotFoundException("Вакансия не найдена по ID: " + vacancyId);
        }

        Long categoryId = parseAndValidateId(editvacancyDto.getCategoryId(), "ID категории");

        if (!categoryDao.existsById(categoryId)) {
            log.error("Попытка обновления вакансии с несуществующей категорией ID: {}", categoryId);
            throw new VacancyNotFoundException("Категория с ID: " + categoryId + " не существует");
        }

        if (editvacancyDto.getExpFrom() > editvacancyDto.getExpTo()) {
            log.error("Некорректный диапазон опыта: от {} до {}", editvacancyDto.getExpFrom(), editvacancyDto.getExpTo());
            throw new IllegalArgumentException("Минимальный опыт не может быть больше максимального");
        }

        validateEditVacancy(editvacancyDto);
        vacancyDao.updateVacancy(vacancyId, editvacancyDto);
        log.info("Вакансия ID {} успешно обновлена", vacancyId);
    }

    @Override
    public void deleteVacancy(Long vacancyId) {
        log.info("Удаление вакансии ID: {}", vacancyId);
        if (vacancyDao.existsVacancy(vacancyId).equals(false)) {
            log.error("Вакансия для удаления не найдена ID: {}", vacancyId);
            throw new VacancyNotFoundException("Вакансия не найдена по ID: " + vacancyId);
        }
        vacancyDao.deleteVacancy(vacancyId);
        log.info("Вакансия ID {} успешно удалена", vacancyId);
    }

    @Override
    public List<VacancyDTO> getAllVacancies() {
        log.info("Получение списка всех вакансий");
        List<VacancyDTO> vacancies = vacancyDao.getAllVacancies().stream().map(this::toDTO).collect(Collectors.toList());
        if (vacancies.isEmpty()) {
            log.warn("Список вакансий пуст");
            throw new VacancyNotFoundException("Вакансий не найдено");
        }
        log.debug("Получено {} вакансий", vacancies.size());
        return vacancies;
    }

    @Override
    public List<VacancyDTO> getRespApplToVacancy(Long applicantId) {
        log.info("Получение вакансий с откликами от соискателя ID: {}", applicantId);
        List<VacancyDTO> vacancies = vacancyDao.getRespondedVacancies(applicantId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        if (vacancies.isEmpty()) {
            log.warn("Не найдено вакансий с откликами от соискателя ID: {}", applicantId);
            throw new VacancyNotFoundException("Не найдено ни одной вакансии, на которую был отклик по ID: " + applicantId);
        }
        log.debug("Найдено {} вакансий с откликами от соискателя ID: {}", vacancies.size(), applicantId);
        return vacancies;
    }

    private VacancyDTO toDTO(Vacancy vacancy) {
        log.trace("Преобразование Vacancy в VacancyDTO для вакансии ID: {}", vacancy.getId());
        return VacancyDTO.builder()
                .authorId(vacancy.getAuthorId())
                .categoryId(vacancy.getCategoryId())
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

    private void validateVacancy(VacancyDTO vacancyDto) {
        log.debug("Валидация вакансии: {}", vacancyDto.getName());
        if (!vacancyDto.getName().matches("^[a-zA-Zа-яА-ЯёЁ\\s]+$") || vacancyDto.getName().length() < 2) {
            log.error("Некорректное название вакансии: {}", vacancyDto.getName());
            throw new IllegalArgumentException("Название должно содержать только буквы и быть не короче 2 символов");
        }
        if (vacancyDto.getDescription().length() < 10) {
            log.error("Слишком короткое описание вакансии: {}", vacancyDto.getName());
            throw new IllegalArgumentException("Описание вакансии должно содержать не менее 10 символов");
        }
        if (vacancyDto.getSalary() < 0) {
            log.error("Отрицательная зарплата в вакансии: {}", vacancyDto.getName());
            throw new IllegalArgumentException("Зарплата не может быть отрицательной");
        }
        if (vacancyDto.getExpFrom() < 0) {
            log.error("Отрицательный минимальный опыт в вакансии: {}", vacancyDto.getName());
            throw new IllegalArgumentException("Минимальный опыт не может быть отрицательным");
        }
        if (vacancyDto.getExpTo() < 0 || vacancyDto.getExpTo() > 50) {
            log.error("Некорректный максимальный опыт в вакансии: {}", vacancyDto.getExpTo());
            throw new IllegalArgumentException("Максимальный опыт не может быть отрицательным и не должен превышать 50 лет");
        }
    }

    private void validateEditVacancy(EditVacancyDTO editVacancyDto) {
        log.debug("Валидация обновляемой вакансии: {}", editVacancyDto.getName());

        if (!editVacancyDto.getName().matches("^[a-zA-Zа-яА-ЯёЁ\\s]+$") || editVacancyDto.getName().length() < 2) {
            log.error("Некорректное название вакансии: {}", editVacancyDto.getName());
            throw new IllegalArgumentException("Название должно содержать только буквы и быть не короче 2 символов");
        }
        if (editVacancyDto.getDescription().length() < 10) {
            log.error("Слишком короткое описание вакансии: {}", editVacancyDto.getName());
            throw new IllegalArgumentException("Описание вакансии должно содержать не менее 10 символов");
        }
        if (editVacancyDto.getSalary() < 0) {
            log.error("Отрицательная зарплата в вакансии: {}", editVacancyDto.getName());
            throw new IllegalArgumentException("Зарплата не может быть отрицательной");
        }
        if (editVacancyDto.getExpFrom() < 0) {
            log.error("Отрицательный минимальный опыт в вакансии: {}", editVacancyDto.getName());
            throw new IllegalArgumentException("Минимальный опыт не может быть отрицательным");
        }
        if (editVacancyDto.getExpTo() < 0 || editVacancyDto.getExpTo() > 50) {
            log.error("Некорректный максимальный опыт в вакансии: {}", editVacancyDto.getExpTo());
            throw new IllegalArgumentException("Максимальный опыт не может быть отрицательным и не должен превышать 50 лет");
        }
    }

    private Long parseAndValidateId(String id, String fieldName) {
        try {
            Long parsedId = Long.parseLong(id);
            if (parsedId <= 0) {
                log.error("{} должен быть положительным числом: {}", fieldName, id);
                throw new IllegalArgumentException(fieldName + " должен быть положительным числом");
            }
            return parsedId;
        } catch (NumberFormatException e) {
            log.error("{} содержит недопустимые символы: {}", fieldName, id);
            throw new IllegalArgumentException(fieldName + " должен содержать только цифры");
        }
    }
}
