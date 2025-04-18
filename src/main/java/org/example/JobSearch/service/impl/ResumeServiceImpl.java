package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.exceptions.CategoryNotFoundException;
import org.example.JobSearch.exceptions.CreateResumeException;
import org.example.JobSearch.exceptions.ResumeNotFoundException;
import org.example.JobSearch.model.*;
import org.example.JobSearch.repository.*;
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
    private final ResumeRepository resumeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EducationInfoService educationInfoService;
    private final WorkExperienceService workExperienceService;

    @Override
    @Transactional(readOnly = true)
    public List<ResumeDTO> getResumesByApplicant(Long applicantId) {
        List<Resume> resumes = resumeRepository.findByApplicantId(applicantId);
        return resumes.stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional
    public void createResume(CreateResumeDTO resumeDto, BindingResult bindingResult) {
        log.info("Создание нового резюме для соискателя ID: {}", resumeDto.getApplicantId());
        validateCreateResume(resumeDto, bindingResult);

        User applicant = userRepository.findById(resumeDto.getApplicantId())
                .orElseThrow(() -> new IllegalArgumentException("Соискатель не найден"));

        Category category = categoryRepository.findById(resumeDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Категория с ID " + resumeDto.getCategoryId() + " не найдена"));

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Resume resume = Resume.builder()
                .applicant(applicant)
                .category(category)
                .name(resumeDto.getName())
                .salary(resumeDto.getSalary())
                .isActive(true)
                .createDate(now)
                .updateTime(now)
                .build();

        Resume savedResume = resumeRepository.save(resume);
        log.info("Резюме создано с ID: {}", savedResume.getId());

        if (resumeDto.getEducationInfos() != null) {
            log.debug("Добавление информации об образовании для резюме ID: {}", savedResume.getId());
            for (EducationInfoDTO eduDto : resumeDto.getEducationInfos()) {
                educationInfoService.createEducationInfo(savedResume.getId(), eduDto);
            }
        }

        if (resumeDto.getWorkExperiences() != null) {
            log.debug("Добавление информации об опыте работы для резюме ID: {}", savedResume.getId());
            for (WorkExperienceDTO expDto : resumeDto.getWorkExperiences()) {
                workExperienceService.createWorkExperience(savedResume.getId(), expDto);
            }
        }
    }

    @Override
    @Transactional
    public void updateResume(Long resumeId, EditResumeDTO editResumeDto) {
        log.info("Обновление резюме ID: {}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Резюме с ID не найдено: " + resumeId));

        if (editResumeDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(editResumeDto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
            resume.setCategory(category);
        }

        if (editResumeDto.getName() != null) {
            resume.setName(editResumeDto.getName());
        }

        if (editResumeDto.getSalary() != null) {
            resume.setSalary(editResumeDto.getSalary());
        }

        if (editResumeDto.getIsActive() != null) {
            resume.setIsActive(editResumeDto.getIsActive());
        }

        resume.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        resumeRepository.save(resume);
    }

    @Override
    @Transactional
    public void deleteResume(Long resumeId) {
        log.info("Удаление резюме ID: {}", resumeId);
        if (!resumeRepository.existsById(resumeId)) {
            throw new ResumeNotFoundException("Резюме с ID не найдено: " + resumeId);
        }
        resumeRepository.deleteById(resumeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeDTO> getAllResumes() {
        List<Resume> resumes = resumeRepository.findByIsActiveTrue();
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("Активные резюме не найдены");
        }
        return resumes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeDTO> getUserResumes(Long applicantId) {
        List<Resume> resumes = resumeRepository.findByApplicantId(applicantId);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("Резюме для соискателя ID не найдены: " + applicantId);
        }
        return resumes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeDTO> getResumesByCategory(Long categoryId) {
        List<Resume> resumes = resumeRepository.findByCategoryIdAndIsActiveTrue(categoryId);
        if (resumes.isEmpty()) {
            throw new CategoryNotFoundException("Резюме по категории ID не найдено: " + categoryId);
        }
        return resumes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeDTO getResumeById(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResumeNotFoundException("Резюме не найдено"));
        return toDTO(resume);
    }

    private ResumeDTO toDTO(Resume resume) {
        ResumeDTO dto = ResumeDTO.builder()
                .id(resume.getId())
                .applicantId(resume.getApplicant().getId())
                .categoryId(resume.getCategory().getId())
                .name(resume.getName())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .createDate(resume.getCreateDate())
                .updateTime(resume.getUpdateTime())
                .build();

        dto.setEducationInfos(educationInfoService.getEducationInfoByResumeId(resume.getId()));
        dto.setWorkExperiences(workExperienceService.getWorkExperienceByResumeId(resume.getId()));

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
}