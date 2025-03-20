package org.example.JobSearch.service.impl;

import org.example.JobSearch.dao.ResumeDao;
import org.example.JobSearch.dto.ResumeDTO;
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
        resumeDao.updateResume(resumeId, resumeDto);
    }

    @Override
    public void deleteResume(Long resumeId) {
        resumeDao.deleteResume(resumeId);
    }

    @Override
    public List<ResumeDTO> getAllResumes() {
        return resumeDao.getAllActiveResumes().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ResumeDTO> getUserResumes(Long applicants_id) {
        return resumeDao.getUserResumes(applicants_id).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ResumeDTO> getResumesByCategory(Long categoryId) {
        return resumeDao.getActiveResumesByCategory(categoryId).stream().map(this::toDTO).collect(Collectors.toList());
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
