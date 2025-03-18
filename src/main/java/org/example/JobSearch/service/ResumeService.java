package org.example.JobSearch.service;


import org.example.JobSearch.dto.ResumeDTO;

import java.util.List;

public interface ResumeService {
    void createResume(ResumeDTO resumeDto);
    void updateResume(Integer resumeId,ResumeDTO resumeDto);
    void deleteResume(Integer resumeId);

    List<ResumeDTO> getAllResumes();

    List<ResumeDTO> getResumesByCategory(Integer categoryId);
}
