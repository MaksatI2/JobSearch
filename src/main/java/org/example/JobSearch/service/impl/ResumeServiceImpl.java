package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dao.CategoryDao;
import org.example.JobSearch.dao.ResumeDao;
import org.example.JobSearch.dto.EditDTO.EditEducationInfoDTO;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.EditDTO.EditWorkExperienceDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.exceptions.CategoryNotFoundException;
import org.example.JobSearch.exceptions.CreateResumeException;
import org.example.JobSearch.exceptions.InvalidUserDataException;
import org.example.JobSearch.exceptions.ResumeNotFoundException;
import org.example.JobSearch.model.Resume;
import org.example.JobSearch.service.EducationInfoService;
import org.example.JobSearch.service.ResumeService;
import org.example.JobSearch.service.WorkExperienceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;
    private final EducationInfoService educationInfoService;
    private final WorkExperienceService workExperienceService;
    private final CategoryDao categoryDao;

    @Override
    public List<ResumeDTO> getResumesByApplicant(Long applicantId) {
        List<Resume> resumes = resumeDao.getUserResumes(applicantId);
        return resumes.stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional
    public void createResume(CreateResumeDTO resumeDto) {
        log.info("Создание нового резюме для соискателя ID: {}", resumeDto.getApplicantId());

        validateCreateResume(resumeDto);

        if (!categoryDao.existsById(resumeDto.getCategoryId())) {
            log.error("Категория с ID {} не найдена", resumeDto.getCategoryId());
            throw new CategoryNotFoundException("Категория с ID " + resumeDto.getCategoryId() + " не найдена");
        }

        if (resumeDto.getUpdateTime() == null) {
            resumeDto.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            log.debug("Установлено время обновления резюме: {}", resumeDto.getUpdateTime());
        }

        Long resumeId = resumeDao.createResume(resumeDto);
        log.info("Резюме создано с ID: {}", resumeId);

        if (resumeDto.getEducationInfos() != null) {
            log.debug("Добавление информации об образовании для резюме ID: {}", resumeId);
            for (EducationInfoDTO eduDto : resumeDto.getEducationInfos()) {
                eduDto.setResumeId(resumeId);
                educationInfoService.createEducationInfo(resumeId, eduDto);
            }
            log.info("Добавлено {} записей об образовании", resumeDto.getEducationInfos().size());
        }

        if (resumeDto.getWorkExperiences() != null) {
            log.debug("Добавление информации об опыте работы для резюме ID: {}", resumeId);
            for (WorkExperienceDTO expDto : resumeDto.getWorkExperiences()) {
                expDto.setResumeId(resumeId);
                workExperienceService.createWorkExperience(resumeId, expDto);
            }
            log.info("Добавлено {} записей об опыте работы", resumeDto.getWorkExperiences().size());
        }
    }

    @Override
    @Transactional
    public void updateResume(Long resumeId, EditResumeDTO editResumeDto) {
        log.info("Обновление резюме ID: {}", resumeId);

        if (!resumeDao.existsResume(resumeId)) {
            throw new ResumeNotFoundException("Резюме с ID не найдено: " + resumeId);
        }

        if (!categoryDao.existsById(editResumeDto.getCategoryId())) {
            log.error("Категория с ID {} не найдена", editResumeDto.getCategoryId());
            throw new CategoryNotFoundException("Категория с ID " + editResumeDto.getCategoryId() + " не найдена");
        }

        resumeDao.updateResume(resumeId, editResumeDto);
        log.info("Основная информация резюме ID {} успешно обновлена", resumeId);

        if (editResumeDto.getEducationInfos() != null) {
            updateEducationInfos(resumeId, editResumeDto.getEducationInfos());
        }

        if (editResumeDto.getWorkExperiences() != null) {
            updateWorkExperiences(resumeId, editResumeDto.getWorkExperiences());
        }
    }

    @Override
    @Transactional
    public void deleteResume(Long resumeId) {
        log.info("Удаление резюме ID: {}", resumeId);
        if (resumeDao.existsResume(resumeId).equals(false)) {
            log.error("Резюме с ID {} не найдено", resumeId);
            throw new ResumeNotFoundException("Резюме с ID не найдено: " + resumeId);
        }
        resumeDao.deleteResume(resumeId);
        log.info("Резюме ID {} успешно удалено", resumeId);
    }

    @Override
    public List<ResumeDTO> getAllResumes() {
        log.info("Получение списка всех активных резюме");
        List<Resume> resumes = resumeDao.getAllActiveResumes();
        if (resumes.isEmpty()) {
            log.warn("Активные резюме не найдены");
            throw new ResumeNotFoundException("Активные резюме не найдены");
        }

        log.debug("Найдено {} активных резюме", resumes.size());
        return resumes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResumeDTO> getUserResumes(Long applicantId) {
        log.info("Получение резюме для соискателя ID: {}", applicantId);
        List<Resume> resumes = resumeDao.getUserResumes(applicantId);
        if (resumes.isEmpty()) {
            log.warn("Резюме для соискателя ID {} не найдены", applicantId);
            throw new ResumeNotFoundException("Резюме для соискателя ID не найдены: " + applicantId);
        }

        log.debug("Найдено {} резюме для соискателя ID {}", resumes.size(), applicantId);
        return resumes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResumeDTO> getResumesByCategory(Long categoryId) {
        log.info("Получение резюме по категории ID: {}", categoryId);
        List<Resume> resumes = resumeDao.getActiveResumesByCategory(categoryId);
        if (resumes.isEmpty()) {
            log.warn("Резюме по категории ID {} не найдены", categoryId);
            throw new CategoryNotFoundException("Резюме по категории ID не найдено: " + categoryId);
        }

        log.debug("Найдено {} резюме по категории ID {}", resumes.size(), categoryId);
        return resumes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ResumeDTO toDTO(Resume resume) {
        log.trace("Преобразование Resume в ResumeDTO для резюме ID: {}", resume.getId());
        ResumeDTO dto = ResumeDTO.builder()
                .applicantId(resume.getApplicantId())
                .categoryId(resume.getCategoryId())
                .name(resume.getName())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .createDate(resume.getCreateDate())
                .updateTime(resume.getUpdateTime())
                .build();

        dto.setEducationInfos(educationInfoService.getEducationInfoByResumeId(resume.getId()));
        log.trace("Добавлена информация об образовании для резюме ID: {}", resume.getId());

        dto.setWorkExperiences(workExperienceService.getWorkExperienceByResumeId(resume.getId()));
        log.trace("Добавлена информация об опыте работы для резюме ID: {}", resume.getId());

        return dto;
    }

    private void updateEducationInfos(Long resumeId, List<EditEducationInfoDTO> educationDtos)  {
        List<EducationInfoDTO> existingEducations = educationInfoService.getEducationInfoByResumeId(resumeId);

        for (EditEducationInfoDTO dto : educationDtos) {

            if (dto.getId() != null) {
                boolean exists = existingEducations.stream()
                        .anyMatch(edu -> edu.getId().equals(dto.getId()));

                if (exists) {
                    EducationInfoDTO updateDto = convertToEducationInfoDTO(dto);
                    educationInfoService.updateEducationInfo(dto.getId(), updateDto);
                } else {
                    log.error("Запись об образовании с ID {} не найдена для резюме {}", dto.getId(), resumeId);
                    throw new InvalidUserDataException("Запись об образовании не найдена: " + dto.getId());
                }
            } else {
                EducationInfoDTO newDto = convertToEducationInfoDTO(dto);
                newDto.setResumeId(resumeId);
                educationInfoService.createEducationInfo(resumeId, newDto);
                log.info("Добавлена новая запись об образовании для резюме {}", resumeId);
            }
        }

        List<Long> incomingIds = educationDtos.stream()
                .map(EditEducationInfoDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        existingEducations.stream()
                .filter(edu -> !incomingIds.contains(edu.getId()))
                .forEach(edu -> {
                    educationInfoService.deleteEducationInfo(edu.getId());
                    log.info("Удалена запись об образовании с ID {} для резюме {}", edu.getId(), resumeId);
                });
    }

    private EducationInfoDTO convertToEducationInfoDTO(EditEducationInfoDTO dto) {
        return EducationInfoDTO.builder()
                .id(dto.getId())
                .institution(dto.getInstitution())
                .program(dto.getProgram())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .degree(dto.getDegree())
                .build();
    }

    private void updateWorkExperiences(Long resumeId, List<EditWorkExperienceDTO> workExperienceDtos) {
        List<WorkExperienceDTO> existingExperiences = workExperienceService.getWorkExperienceByResumeId(resumeId);

        for (EditWorkExperienceDTO dto : workExperienceDtos) {

            if (dto.getId() != null) {
                boolean exists = existingExperiences.stream()
                        .anyMatch(exp -> exp.getId().equals(dto.getId()));

                if (exists) {
                    WorkExperienceDTO updateDto = convertToWorkExperienceDTO(dto);
                    workExperienceService.updateWorkExperience(dto.getId(), updateDto);
                } else {
                    log.error("Запись об опыте работы с ID {} не найдена для резюме {}", dto.getId(), resumeId);
                    throw new InvalidUserDataException("Запись об опыте работы не найдена: " + dto.getId());
                }
            } else {
                WorkExperienceDTO newDto = convertToWorkExperienceDTO(dto);
                newDto.setResumeId(resumeId);
                workExperienceService.createWorkExperience(resumeId, newDto);
                log.info("Добавлена новая запись об опыте работы для резюме {}", resumeId);
            }
        }

        List<Long> incomingIds = workExperienceDtos.stream()
                .map(EditWorkExperienceDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        existingExperiences.stream()
                .filter(exp -> !incomingIds.contains(exp.getId()))
                .forEach(exp -> {
                    workExperienceService.deleteWorkExperience(exp.getId());
                    log.info("Удалена запись об опыте работы с ID {} для резюме {}", exp.getId(), resumeId);
                });
    }

    private WorkExperienceDTO convertToWorkExperienceDTO(EditWorkExperienceDTO dto) {
        return WorkExperienceDTO.builder()
                .id(dto.getId())
                .years(dto.getYears())
                .companyName(dto.getCompanyName())
                .position(dto.getPosition())
                .responsibilities(dto.getResponsibilities())
                .build();
    }

    @Override
    public void validateCreateResume(CreateResumeDTO resumeDto) {
        if (resumeDto.getCategoryId() == null) {
            throw new CreateResumeException("categoryId", "категории не может быть пустым");
        }

        if (resumeDto.getName() == null || resumeDto.getName().trim().isEmpty()) {
            throw new CreateResumeException("name", "Название резюме не может быть пустым");
        }

        if (resumeDto.getName().length() < 8) {
            throw new CreateResumeException("name", "Название резюме должно содержать минимум 8 символов");
        }

        if (resumeDto.getName().matches(".*\\d.*")) {
            throw new CreateResumeException("name", "Название резюме не должно содержать цифры");
        }

        if (resumeDto.getSalary() != null) {
            if (resumeDto.getSalary() < 0) {
                throw new CreateResumeException("salary", "Зарплата не может быть отрицательной");
            }
        }

    }

    @Override
    public EditResumeDTO convertToEditDTO(ResumeDTO resume) {

        EditResumeDTO editResumeDTO = EditResumeDTO.builder()
                .categoryId(resume.getCategoryId())
                .name(resume.getName())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .build();

        // Преобразование образования
        if (resume.getEducationInfos() != null && !resume.getEducationInfos().isEmpty()) {
            List<EditEducationInfoDTO> educationInfos = resume.getEducationInfos().stream()
                    .map(this::convertToEditEducationInfoDTO)
                    .collect(Collectors.toList());
            editResumeDTO.setEducationInfos(educationInfos);
        }

        // Преобразование опыта работы
        if (resume.getWorkExperiences() != null && !resume.getWorkExperiences().isEmpty()) {
            List<EditWorkExperienceDTO> workExperiences = resume.getWorkExperiences().stream()
                    .map(this::convertToEditWorkExperienceDTO)
                    .collect(Collectors.toList());
            editResumeDTO.setWorkExperiences(workExperiences);
        }

        return editResumeDTO;
    }

    private EditEducationInfoDTO convertToEditEducationInfoDTO(EducationInfoDTO dto) {
        return EditEducationInfoDTO.builder()
                .id(dto.getId())
                .institution(dto.getInstitution())
                .program(dto.getProgram())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .degree(dto.getDegree())
                .build();
    }

    private EditWorkExperienceDTO convertToEditWorkExperienceDTO(WorkExperienceDTO dto) {
        return EditWorkExperienceDTO.builder()
                .id(dto.getId())
                .years(dto.getYears())
                .companyName(dto.getCompanyName())
                .position(dto.getPosition())
                .responsibilities(dto.getResponsibilities())
                .build();
    }

    @Override
    public ResumeDTO getResumeById(Long id) {
        log.info("Получение резюме по ID: {}", id);
        Resume resume = resumeDao.getResumeById(id);

        ResumeDTO dto = toDTO(resume);

        dto.setEducationInfos(educationInfoService.getEducationInfoByResumeId(id));
        dto.setWorkExperiences(workExperienceService.getWorkExperienceByResumeId(id));

        return dto;
    }
}