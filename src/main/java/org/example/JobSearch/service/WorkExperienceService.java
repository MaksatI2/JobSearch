package org.example.JobSearch.service;

import org.example.JobSearch.dto.WorkExperienceDTO;

import java.util.List;

public interface WorkExperienceService {

    void createWorkExperience(Long resumeId, WorkExperienceDTO workExperienceDto);
    void deleteByResumeId(Long resumeId);

    List<WorkExperienceDTO> getWorkExperienceByResumeId(Long resumeId);
}