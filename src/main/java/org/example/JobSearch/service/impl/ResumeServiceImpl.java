package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
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
        if (resumeDto.getApplicantId() == null) {
            throw new InvalidUserDataException("Applicant ID cannot be null");
        }

        if (!userDao.isUserApplicant(resumeDto.getApplicantId())) {
            throw new InvalidUserDataException("Only applicants can create resumes");
        }

        if (resumeDto.getUpdateTime() == null) {
            resumeDto.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }

        Long resumeId = resumeDao.createResume(resumeDto);

        if (resumeDto.getEducationInfos() != null) {
            for (EducationInfoDTO eduDto : resumeDto.getEducationInfos()) {
                eduDto.setResumeId(resumeId);
                educationInfoService.createEducationInfo(resumeId, eduDto);
            }
        }

        if (resumeDto.getWorkExperiences() != null) {
            for (WorkExperienceDTO expDto : resumeDto.getWorkExperiences()) {
                expDto.setResumeId(resumeId);
                workExperienceService.createWorkExperience(resumeId, expDto);
            }
        }
    }

    @Override
    public void updateResume(Long resumeId, ResumeDTO resumeDto) {
        Resume existingResume = resumeDao.getResumeById(resumeId);
        if (existingResume == null) {
            throw new ResumeNotFoundException("Resume not found with ID: " + resumeId);
        }

        if (resumeDto.getApplicantId() == null) {
            throw new InvalidUserDataException("Applicant ID cannot be null");
        }

        if (!userDao.isUserApplicant(resumeDto.getApplicantId())) {
            throw new InvalidUserDataException("Only applicants can update resumes");
        }

        if (!existingResume.getApplicantId().equals(resumeDto.getApplicantId())) {
            throw new InvalidUserDataException("You can only update your own resumes");
        }

        resumeDao.updateResume(resumeId, resumeDto);
    }

    @Override
    public void deleteResume(Long resumeId) {
        Resume resume = resumeDao.getResumeById(resumeId);
        if (resume == null) {
            throw new ResumeNotFoundException("Resume not found with ID: " + resumeId);
        }
        resumeDao.deleteResume(resumeId);
    }

    @Override
    public List<ResumeDTO> getAllResumes() {
        List<Resume> resumes = resumeDao.getAllActiveResumes();
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("No active resumes found");
        }

        return resumes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResumeDTO> getUserResumes(Long applicantId) {
        List<Resume> resumes = resumeDao.getUserResumes(applicantId);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("No resumes found for applicant ID: " + applicantId);
        }

        return resumes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResumeDTO> getResumesByCategory(Long categoryId) {
        List<Resume> resumes = resumeDao.getActiveResumesByCategory(categoryId);
        if (resumes.isEmpty()) {
            throw new CategoryNotFoundException("Резюме по категории ID не найдено: " + categoryId);
        }

        return resumes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ResumeDTO toDTO(Resume resume) {
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

        dto.setWorkExperiences(workExperienceService.getWorkExperienceByResumeId(resume.getId()));

        return dto;
    }
}