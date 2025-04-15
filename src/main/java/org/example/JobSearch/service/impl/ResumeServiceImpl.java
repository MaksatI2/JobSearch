package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dao.CategoryDao;
import org.example.JobSearch.dao.ResumeDao;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.exceptions.CategoryNotFoundException;
import org.example.JobSearch.exceptions.CreateResumeException;
import org.example.JobSearch.exceptions.ResumeNotFoundException;
import org.example.JobSearch.model.Resume;
import org.example.JobSearch.service.EducationInfoService;
import org.example.JobSearch.service.ResumeService;
import org.example.JobSearch.service.WorkExperienceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.sql.Timestamp;
import java.util.List;
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
    public void createResume(CreateResumeDTO resumeDto, BindingResult bindingResult) {
        log.info("Создание нового резюме для соискателя ID: {}", resumeDto.getApplicantId());

        validateCreateResume(resumeDto, bindingResult);

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

    @Transactional
    @Override
    public void updateResume(Long resumeId, EditResumeDTO editResumeDto) {
        log.info("Обновление резюме ID: {}", resumeId);

        if (!resumeDao.existsResume(resumeId)) {
            throw new ResumeNotFoundException("Резюме с ID не найдено: " + resumeId);
        }

        resumeDao.updateResume(resumeId, editResumeDto);
        log.info("Основная информация резюме ID {} успешно обновлена", resumeId);
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
                .id(resume.getId())
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

    @Override
    public void validateCreateResume(CreateResumeDTO resumeDto, BindingResult bindingResult) {
        if (resumeDto.getCategoryId() == null) {
            throw new CreateResumeException("categoryId", "категории не может быть пустым");
        }

        if (resumeDto.getName() == null || resumeDto.getName().trim().isEmpty()) {
            throw new CreateResumeException("name", "Название резюме не может быть пустым");
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
    public ResumeDTO getResumeById(Long id) {
        log.info("Получение резюме по ID: {}", id);
        return resumeDao.getResumeById(id);
    }
}