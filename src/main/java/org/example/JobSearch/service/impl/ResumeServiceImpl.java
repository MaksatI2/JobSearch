package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.ContactInfoDTO;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
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
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
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
        User applicant = userService.getUserId(resumeDto.getApplicantId());
        validateCreateResume(resumeDto, bindingResult, applicant.getAge());

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
    public void validateCreateResume(CreateResumeDTO resumeDto, BindingResult bindingResult, Integer userAge) {
        if (resumeDto.getWorkExperiences() != null && !resumeDto.getWorkExperiences().isEmpty()) {
            validateWorkExperience(resumeDto.getWorkExperiences(), userAge, bindingResult);
        }

        if (resumeDto.getEducationInfos() != null && !resumeDto.getEducationInfos().isEmpty()) {
            validateEducation(resumeDto.getEducationInfos(), userAge, bindingResult);
        }

        if (resumeDto.getContactInfos() != null) {
            for (int i = 0; i < resumeDto.getContactInfos().size(); i++) {
                ContactInfoDTO contact = resumeDto.getContactInfos().get(i);
                String value = contact.getValue();
                Long typeId = contact.getTypeId();

                if (value == null || value.trim().isEmpty()) {
                    continue;
                }

                switch (typeId.intValue()) {
                    case 1:
                        if (!value.matches("^996\\d{9}$")) {
                            bindingResult.rejectValue("contactInfos[" + i + "].value",
                                    "error.resumeForm", getMessage("contact.phone.invalid"));
                        }
                        break;
                    case 2:
                        if (!value.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                            bindingResult.rejectValue("contactInfos[" + i + "].value",
                                    "error.resumeForm", getMessage("contact.email.invalid"));
                        }
                        break;
                    case 3:
                        if (!value.matches("^https://(www\\.)?linkedin\\.com/in/.+")) {
                            bindingResult.rejectValue("contactInfos[" + i + "].value",
                                    "error.resumeForm", getMessage("contact.linkedin.invalid"));
                        }
                        break;
                    case 4:
                        if (!value.matches("^https://(www\\.)?github\\.com/.+")) {
                            bindingResult.rejectValue("contactInfos[" + i + "].value",
                                    "error.resumeForm", getMessage("contact.github.invalid"));
                        }
                        break;
                    case 5:
                        if (!value.matches("^@\\w{5,}$") && !value.matches("^996\\d{9}$")) {
                            bindingResult.rejectValue("contactInfos[" + i + "].value",
                                    "error.resumeForm", getMessage("contact.telegram.invalid"));
                        }
                        break;
                    case 6:
                        if (!value.matches("^(https?://)?[\\w.-]+\\.[a-z]{2,6}.*$")) {
                            bindingResult.rejectValue("contactInfos[" + i + "].value",
                                    "error.resumeForm", getMessage("contact.website.invalid"));
                        }
                        break;
                    default:
                        bindingResult.rejectValue("contactInfos[" + i + "].typeId",
                                "error.resumeForm", getMessage("contact.type.invalid"));
                }
            }
        }
    }

    @Override
    public void validateEducation(List<EducationInfoDTO> educationInfos, Integer userAge, BindingResult bindingResult) {
        if (educationInfos == null || educationInfos.isEmpty()) {
            return;
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        LocalDate currentDate = now.toLocalDateTime().toLocalDate();
        LocalDate userAge16Date = currentDate.minusYears(userAge - 16);

        for (int i = 0; i < educationInfos.size(); i++) {
            EducationInfoDTO eduDto = educationInfos.get(i);

            if (eduDto.getStartDate().after(now)) {
                bindingResult.rejectValue("educationInfos[" + i + "].startDate",
                        "error.resumeForm", getMessage("education.start.future"));
            }

            if (eduDto.getEndDate().after(now)) {
                bindingResult.rejectValue("educationInfos[" + i + "].endDate",
                        "error.resumeForm", getMessage("education.end.future"));
            }

            if (eduDto.getStartDate() != null && eduDto.getEndDate() != null
                && eduDto.getStartDate().after(eduDto.getEndDate())) {
                bindingResult.rejectValue("educationInfos[" + i + "].startDate",
                        "error.resumeForm", getMessage("education.period.invalid"));
            }

            LocalDate startDate = eduDto.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (startDate.isBefore(userAge16Date)) {
                bindingResult.rejectValue("educationInfos[" + i + "].startDate",
                        "error.resumeForm", getMessage("education.start.too.early"));
            }
        }
    }

    public void validateWorkExperience(List<WorkExperienceDTO> workExperiences, Integer userAge, BindingResult bindingResult) {
        if (workExperiences == null || workExperiences.isEmpty()) {
            return;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate userAge18Date = currentDate.minusYears(userAge - 18);

        for (int i = 0; i < workExperiences.size(); i++) {
            WorkExperienceDTO workExp = workExperiences.get(i);

            int totalMonths = (workExp.getYears() != null ? workExp.getYears() * 12 : 0) +
                              (workExp.getMonths() != null ? workExp.getMonths() : 0);
            int maxPossibleMonths = Period.between(userAge18Date, currentDate).getYears() * 12 +
                                    Period.between(userAge18Date, currentDate).getMonths();

            if (totalMonths > maxPossibleMonths) {
                bindingResult.rejectValue("workExperiences[" + i + "].years",
                        "error.resumeForm", getMessage("resume.work.experience.too.long"));
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