package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.ContactInfoDTO;
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
import org.example.JobSearch.service.ContactInfoService;
import org.example.JobSearch.service.EducationInfoService;
import org.example.JobSearch.service.ResumeService;
import org.example.JobSearch.service.WorkExperienceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ContactInfoService contactInfoService;

    @Transactional(readOnly = true)
    @Override
    public Page<ResumeDTO> getResumesByApplicant(Long applicantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Resume> resumesPage = resumeRepository.findByApplicantId(applicantId, pageable);
        return resumesPage.map(this::toDTO);
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

        if (resumeDto.getContactInfos() != null) {
            log.debug("Добавление контактной информации для резюме ID: {}", savedResume.getId());
            for (ContactInfoDTO contactDto : resumeDto.getContactInfos()) {
                if (contactDto.getValue() != null && !contactDto.getValue().isEmpty()) {
                    contactInfoService.createContactInfo(savedResume.getId(), contactDto);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateResume(Long resumeId, EditResumeDTO editResumeDto) {
        log.info("Обновление резюме ID: {}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Резюме с ID не найдено: " + resumeId));

        if (editResumeDto.getEducationInfos() != null) {
            updateEducationInfo(resume, editResumeDto.getEducationInfos());
        }

        if (editResumeDto.getWorkExperiences() != null) {
            updateWorkExperience(resume, editResumeDto.getWorkExperiences());
        }

        resume.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        resumeRepository.save(resume);
    }

    private void updateEducationInfo(Resume resume, List<EducationInfoDTO> educationInfoDTOs) {
        educationInfoService.deleteByResumeId(resume.getId());

        for (EducationInfoDTO eduDto : educationInfoDTOs) {
            educationInfoService.createEducationInfo(resume.getId(), eduDto);
        }
    }

    private void updateWorkExperience(Resume resume, List<WorkExperienceDTO> workExperienceDTOs) {
        workExperienceService.deleteByResumeId(resume.getId());

        for (WorkExperienceDTO expDto : workExperienceDTOs) {
            workExperienceService.createWorkExperience(resume.getId(), expDto);
        }
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
    @Transactional
    public void refreshResume(Long resumeId) {
        resumeRepository.refreshResume(resumeId);
    }


    @Transactional(readOnly = true)
    @Override
    public Page<ResumeDTO> getAllResumes(Pageable pageable) {
        Page<Resume> resumes = resumeRepository.findByIsActiveTrue(pageable);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("Активные резюме не найдены");
        }
        return resumes.map(this::toDTO);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<ResumeDTO> getUserResumes(Long applicantId) {
//        List<Resume> resumes = resumeRepository.findByApplicantId(applicantId);
//        if (resumes.isEmpty()) {
//            throw new ResumeNotFoundException("Резюме для соискателя ID не найдены: " + applicantId);
//        }
//        return resumes.stream().map(this::toDTO).collect(Collectors.toList());
//    }

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
                .applicantName(resume.getApplicant().getName())
                .categoryId(resume.getCategory().getId())
                .name(resume.getName())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .createDate(resume.getCreateDate())
                .updateTime(resume.getUpdateTime())
                .build();

        dto.setEducationInfos(educationInfoService.getEducationInfoByResumeId(resume.getId()));
        dto.setWorkExperiences(workExperienceService.getWorkExperienceByResumeId(resume.getId()));
        dto.setContactInfos(contactInfoService.getContactInfoByResumeId(resume.getId()));

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
    public void validateEducation(List<EducationInfoDTO> educationInfos, BindingResult bindingResult) {
        if (educationInfos == null || educationInfos.isEmpty()) {
            return;
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (EducationInfoDTO eduDto : educationInfos) {
            if (eduDto.getStartDate() == null) {
                throw new CreateResumeException("startDate", "Дата начала обучения не может быть пустой");
            }

            if (eduDto.getEndDate() == null) {
                throw new CreateResumeException("endDate", "Дата окончания обучения не может быть пустой");
            }

            if (eduDto.getStartDate().after(now)) {
                throw new CreateResumeException("startDate", "Дата начала обучения не может быть в будущем");
            }

            if (eduDto.getEndDate().after(now)) {
                throw new CreateResumeException("endDate", "Дата окончания обучения не может быть в будущем");
            }

            if (eduDto.getStartDate().after(eduDto.getEndDate())) {
                throw new CreateResumeException("startDate", "Дата начала обучения не может быть позже даты окончания");
            }
        }
    }
}