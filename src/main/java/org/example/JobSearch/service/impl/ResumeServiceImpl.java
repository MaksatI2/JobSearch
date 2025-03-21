package org.example.JobSearch.service.impl;

import org.example.JobSearch.dao.ResumeDao;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.exceptions.ResumeNotFoundException;
import org.example.JobSearch.model.Resume;
import org.example.JobSearch.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;

    @Override
    public void createResume(ResumeDTO resumeDto) {
        resumeDao.createResume(resumeDto);
    }

    @Override
    public void updateResume(Long resumeId, ResumeDTO resumeDto) {
        if (resumeDao.getResumeById(resumeId) == null) {
            throw new ResumeNotFoundException("Resume not found with ID: " + resumeId);
        }
        resumeDao.updateResume(resumeId, resumeDto);
    }

    @Override
    public void deleteResume(Long resumeId) {
        if (resumeDao.getResumeById(resumeId) == null) {
            throw new ResumeNotFoundException("Resume not found with ID: " + resumeId);
        }
        resumeDao.deleteResume(resumeId);
    }

    @Override
    public List<ResumeDTO> getAllResumes() {
        List<ResumeDTO> resumes = resumeDao.getAllActiveResumes().stream().map(this::toDTO).collect(Collectors.toList());
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("No active resumes found");
        }
        return resumes;
    }

    @Override
    public List<ResumeDTO> getUserResumes(Long applicantId) {
        List<ResumeDTO> resumes = resumeDao.getUserResumes(applicantId).stream().map(this::toDTO).collect(Collectors.toList());
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("No resumes found for applicant ID: " + applicantId);
        }
        return resumes;
    }

    @Override
    public List<ResumeDTO> getResumesByCategory(Long categoryId) {
        List<ResumeDTO> resumes = resumeDao.getActiveResumesByCategory(categoryId).stream().map(this::toDTO).collect(Collectors.toList());
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("No resumes found for category ID: " + categoryId);
        }
        return resumes;
    }

    private ResumeDTO toDTO(Resume resume) {
        return ResumeDTO.builder()
                .id(resume.getId())
                .applicantId(resume.getApplicantId())
                .categoryId(resume.getCategoryId())
                .name(resume.getName())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .updateTime(resume.getUpdateTime())
                .build();
    }
}