package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.ContactInfoDTO;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.exceptions.CreateResumeException;
import org.example.JobSearch.exceptions.ResumeNotFoundException;
import org.example.JobSearch.model.Category;
import org.example.JobSearch.model.Resume;
import org.example.JobSearch.model.User;
import org.example.JobSearch.repository.ResumeRepository;
import org.example.JobSearch.service.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final EducationInfoService educationInfoService;
    private final WorkExperienceService workExperienceService;
    private final ContactInfoService contactInfoService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final MessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    public Resume getResumeEntityById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new ResumeNotFoundException(getMessage("resume.not.found.with.id") + " " + id));
    }

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

        User applicant = userService.getUserId(resumeDto.getApplicantId());

        Category category = categoryService.getCategoryById(resumeDto.getCategoryId());

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
                .orElseThrow(() -> new ResumeNotFoundException(getMessage("resume.not.found.with.id") + " " + resumeId));

        if (editResumeDto.getEducationInfos() != null) {
            updateEducationInfo(resume, editResumeDto.getEducationInfos());
        }

        if (editResumeDto.getWorkExperiences() != null) {
            updateWorkExperience(resume, editResumeDto.getWorkExperiences());
        }

        if (editResumeDto.getContactInfos() != null) {
            updateContactInfo(resume, editResumeDto.getContactInfos());
        }

        resume.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        resumeRepository.save(resume);
    }

    private void updateContactInfo(Resume resume, List<ContactInfoDTO> contactInfoDTOs) {
        contactInfoService.deleteByResumeId(resume.getId());

        for (ContactInfoDTO contactDto : contactInfoDTOs) {
            if (contactDto.getValue() != null && !contactDto.getValue().isEmpty()) {
                contactInfoService.createContactInfo(resume.getId(), contactDto);
            }
        }
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
            throw new ResumeNotFoundException(getMessage("resume.not.found.with.id") + " " + resumeId);
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
    public Page<ResumeDTO> getAllResumes(String sort, Pageable pageable) {
        Page<Resume> resumes = resumeRepository.findAllActiveSorted(sort, pageable);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException(getMessage("resume.not.found.active"));
        }
        return resumes.map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeDTO getResumeById(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResumeNotFoundException(getMessage("resume.not.found")));
        return toDTO(resume);
    }

    private ResumeDTO toDTO(Resume resume) {
        int responsesCount = resume.getRespondedApplicants().size();
        ResumeDTO dto = ResumeDTO.builder()
                .id(resume.getId())
                .applicantId(resume.getApplicant().getId())
                .applicantName(resume.getApplicant().getName())
                .applicantAge(resume.getApplicant().getAge())
                .applicantAvatar(resume.getApplicant().getAvatar())
                .categoryId(resume.getCategory().getId())
                .name(resume.getName())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .createDate(resume.getCreateDate())
                .updateTime(resume.getUpdateTime())
                .responsesCount(responsesCount)
                .build();

        dto.setEducationInfos(educationInfoService.getEducationInfoByResumeId(resume.getId()));
        dto.setWorkExperiences(workExperienceService.getWorkExperienceByResumeId(resume.getId()));
        dto.setContactInfos(contactInfoService.getContactInfoByResumeId(resume.getId()));

        return dto;
    }

    @Override
    public void validateCreateResume(CreateResumeDTO resumeDto, BindingResult bindingResult) {
        if (resumeDto.getCategoryId() == null) {
            throw new CreateResumeException("categoryId", getMessage("resume.category.empty"));
        }

        if (resumeDto.getName() == null || resumeDto.getName().trim().isEmpty()) {
            throw new CreateResumeException("name", getMessage("resume.name.empty"));
        }

        if (resumeDto.getName().matches(".*\\d.*")) {
            throw new CreateResumeException("name", getMessage("resume.name.numbers"));
        }

        if (resumeDto.getSalary() != null) {
            if (resumeDto.getSalary() < 0) {
                throw new CreateResumeException("salary", getMessage("resume.salary.negative"));
            }
        }

        if (resumeDto.getContactInfos() != null) {
            List<ContactInfoDTO> contactInfos = resumeDto.getContactInfos();

            for (int i = 0; i < contactInfos.size(); i++) {
                ContactInfoDTO contact = contactInfos.get(i);
                String value = contact.getValue();
                Long typeId = contact.getTypeId();

                if (value == null || value.trim().isEmpty()) {
                    continue;
                }

                if (typeId == 1 && !value.matches("^996\\d{9}$")) {
                    throw new CreateResumeException("contactInfos[" + i + "].value",
                            getMessage("contact.phone.invalid"));
                }

                if (typeId == 2 && !value.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                    throw new CreateResumeException("contactInfos[" + i + "].value",
                            getMessage("contact.email.invalid"));
                }

                if (typeId == 3 && !value.matches("^https://(www\\.)?linkedin\\.com/in/.+")) {
                    throw new CreateResumeException("contactInfos[" + i + "].value",
                            getMessage("contact.linkedin.invalid"));
                }

                if (typeId == 4 && !value.matches("^https://(www\\.)?github\\.com/.+")) {
                    throw new CreateResumeException("contactInfos[" + i + "].value",
                            getMessage("contact.github.invalid"));
                }

                if (typeId == 5 && !value.matches("^@\\w{5,}$") && !value.matches("^996\\d{9}$")) {
                    throw new CreateResumeException("contactInfos[" + i + "].value",
                            getMessage("contact.telegram.invalid"));
                }

                if (typeId == 6 && !value.matches("^(https?://)?[\\w.-]+\\.[a-z]{2,6}.*$")) {
                    throw new CreateResumeException("contactInfos[" + i + "].value",
                            getMessage("contact.website.invalid"));
                }
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
                throw new CreateResumeException("startDate", getMessage("education.start.empty"));
            }

            if (eduDto.getEndDate() == null) {
                throw new CreateResumeException("endDate", getMessage("education.end.empty"));
            }

            if (eduDto.getStartDate().after(now)) {
                throw new CreateResumeException("startDate", getMessage("education.start.future"));
            }

            if (eduDto.getEndDate().after(now)) {
                throw new CreateResumeException("endDate", getMessage("education.end.future"));
            }

            if (eduDto.getStartDate().after(eduDto.getEndDate())) {
                throw new CreateResumeException("startDate", getMessage("education.period.invalid"));
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResumeDTO> getResumesWithResponsesByApplicantId(Long applicantId, Pageable pageable) {
        Page<Resume> resumesPage = resumeRepository.findResumesWithResponsesByApplicantId(applicantId, pageable);
        return resumesPage.map(this::toDTO);
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}