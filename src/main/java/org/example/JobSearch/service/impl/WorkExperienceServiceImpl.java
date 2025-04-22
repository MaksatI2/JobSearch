package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.model.Resume;
import org.example.JobSearch.model.WorkExperience;
import org.example.JobSearch.repository.WorkExperienceRepository;
import org.example.JobSearch.service.WorkExperienceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkExperienceServiceImpl implements WorkExperienceService {
    private final WorkExperienceRepository workExperienceRepository;

    @Override
    @Transactional
    public void createWorkExperience(Long resumeId, WorkExperienceDTO workExperienceDto) {
        WorkExperience workExperience = new WorkExperience();

        Resume resume = new Resume();
        resume.setId(resumeId);
        workExperience.setResume(resume);
        workExperience.setYears(workExperienceDto.getYears());
        workExperience.setCompanyName(workExperienceDto.getCompanyName());
        workExperience.setPosition(workExperienceDto.getPosition());
        workExperience.setResponsibilities(workExperienceDto.getResponsibilities());

        workExperienceRepository.save(workExperience);
    }

    @Override
    @Transactional
    public void deleteByResumeId(Long resumeId) {
        workExperienceRepository.deleteByResumeId(resumeId);
    }

    @Override
    public List<WorkExperienceDTO> getWorkExperienceByResumeId(Long resumeId) {
        return workExperienceRepository.findByResumeId(resumeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private WorkExperienceDTO toDTO(WorkExperience workExperience) {
        return WorkExperienceDTO.builder()
                .id(workExperience.getId())
                .resumeId(workExperience.getResume().getId())
                .years(workExperience.getYears())
                .companyName(workExperience.getCompanyName())
                .position(workExperience.getPosition())
                .responsibilities(workExperience.getResponsibilities())
                .build();
    }
}