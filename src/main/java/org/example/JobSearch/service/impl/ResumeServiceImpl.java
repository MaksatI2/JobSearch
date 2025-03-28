package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dao.ResumeDao;
import org.example.JobSearch.dao.UserDao;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.exceptions.CategoryNotFoundException;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;
    private final UserDao userDao;
    private final EducationInfoService educationInfoService;
    private final WorkExperienceService workExperienceService;

    @Override
    @Transactional
    public void createResume(ResumeDTO resumeDto) {
        log.info("Создание нового резюме для соискателя ID: {}", resumeDto.getApplicantId());

        if (resumeDto.getApplicantId() == null) {
            log.error("ID соискателя не может быть null");
            throw new InvalidUserDataException("ID соискателя не может быть null");
        }

        if (!userDao.isUserApplicant(resumeDto.getApplicantId())) {
            log.error("Попытка создания резюме пользователем, не являющимся соискателем: {}", resumeDto.getApplicantId());
            throw new InvalidUserDataException("Только соискатели могут создавать резюме");
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

    public void updateResume(Long resumeId, ResumeDTO resumeDto) {
        log.info("Обновление резюме ID: {}", resumeId);
        Resume existingResume = resumeDao.getResumeById(resumeId);
        if (existingResume == null) {
            log.error("Резюме с ID {} не найдено", resumeId);
            throw new ResumeNotFoundException("Резюме с ID не найдено: " + resumeId);
        }

        if (resumeDto.getApplicantId() == null) {
            log.error("ID соискателя не может быть null");
            throw new InvalidUserDataException("ID соискателя не может быть null");
        }

        if (!userDao.isUserApplicant(resumeDto.getApplicantId())) {
            log.error("Попытка обновления резюме пользователем, не являющимся соискателем: {}", resumeDto.getApplicantId());
            throw new InvalidUserDataException("Только соискатели могут обновлять резюме");
        }

        if (!existingResume.getApplicantId().equals(resumeDto.getApplicantId())) {
            log.error("Попытка обновления чужого резюме. Ожидался соискатель ID: {}, фактический ID: {}",
                    existingResume.getApplicantId(), resumeDto.getApplicantId());
            throw new InvalidUserDataException("Вы можете обновлять только свои резюме");
        }

        // Обновляем основную информацию резюме
        resumeDao.updateResume(resumeId, resumeDto);
        log.info("Основная информация резюме ID {} успешно обновлена", resumeId);

        // Обработка информации об образовании
        if (resumeDto.getEducationInfos() != null) {
            log.debug("Обновление информации об образовании для резюме ID: {}", resumeId);
            for (EducationInfoDTO eduDto : resumeDto.getEducationInfos()) {
                // Запрещаем указывать ID, чтобы нельзя было создать новые записи
                if (eduDto.getId() != null) {
                    log.error("Попытка указать ID для образования в обновлении резюме");
                    throw new InvalidUserDataException("Нельзя указывать ID для образования при обновлении резюме");
                }
                eduDto.setResumeId(resumeId);
                educationInfoService.createEducationInfo(resumeId, eduDto);
            }
            log.info("Обновлено {} записей об образовании", resumeDto.getEducationInfos().size());
        }

        // Обработка информации об опыте работы
        if (resumeDto.getWorkExperiences() != null) {
            log.debug("Обновление информации об опыте работы для резюме ID: {}", resumeId);
            for (WorkExperienceDTO expDto : resumeDto.getWorkExperiences()) {
                // Запрещаем указывать ID, чтобы нельзя было создать новые записи
                if (expDto.getId() != null) {
                    log.error("Попытка указать ID для опыта работы в обновлении резюме");
                    throw new InvalidUserDataException("Нельзя указывать ID для опыта работы при обновлении резюме");
                }
                expDto.setResumeId(resumeId);
                workExperienceService.createWorkExperience(resumeId, expDto);
            }
            log.info("Обновлено {} записей об опыте работы", resumeDto.getWorkExperiences().size());
        }
    }

    @Override
    @Transactional
    public void deleteResume(Long resumeId) {
        log.info("Удаление резюме ID: {}", resumeId);
        Resume existingResume = resumeDao.getResumeById(resumeId);
        if (existingResume == null) {
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
}