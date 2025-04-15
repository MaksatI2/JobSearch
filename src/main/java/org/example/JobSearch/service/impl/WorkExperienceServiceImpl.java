package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.WorkExperienceDao;
import org.example.JobSearch.dto.WorkExperienceDTO;
import org.example.JobSearch.model.WorkExperience;
import org.example.JobSearch.service.WorkExperienceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkExperienceServiceImpl implements WorkExperienceService {
    private final WorkExperienceDao workExperienceDao;

    @Override
    public void createWorkExperience(Long resumeId, WorkExperienceDTO workExperienceDto) {

        WorkExperience workExperience = new WorkExperience();
        workExperience.setYears(workExperienceDto.getYears());
        workExperience.setCompanyName(workExperienceDto.getCompanyName());
        workExperience.setPosition(workExperienceDto.getPosition());
        workExperience.setResponsibilities(workExperienceDto.getResponsibilities());

        workExperienceDao.createWorkExperience(resumeId, workExperience);
    }

    @Override
    public List<WorkExperienceDTO> getWorkExperienceByResumeId(Long resumeId) {
        return workExperienceDao.getWorkExperienceByResumeId(resumeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private WorkExperienceDTO toDTO(WorkExperience workExperience) {
        return WorkExperienceDTO.builder()
                .id(workExperience.getId())
                .resumeId(workExperience.getResumeId())
                .years(workExperience.getYears())
                .companyName(workExperience.getCompanyName())
                .position(workExperience.getPosition())
                .responsibilities(workExperience.getResponsibilities())
                .build();
    }
}