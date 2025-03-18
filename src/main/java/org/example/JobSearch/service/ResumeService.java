package org.example.JobSearch.service;


import org.example.JobSearch.dto.ResumeDTO;

import java.util.List;

public interface ResumeService {
    void createResume(ResumeDTO resumeDto);
    void updateResume(Long resumeId,ResumeDTO resumeDto);
    void deleteResume(Long resumeId);

    List<ResumeDTO> getAllResumes();

    List<ResumeDTO> getResumesByCategory(Long categoryId);
}
