package org.example.JobSearch.service;


import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.ResumeDTO;

import java.util.List;

public interface ResumeService {
    void createResume(ResumeDTO resumeDto);
    void updateResume(Long resumeId, EditResumeDTO editresumeDto);
    void deleteResume(Long resumeId);

    List<ResumeDTO> getAllResumes();

    List<ResumeDTO> getUserResumes(Long applicants_id);

    List<ResumeDTO> getResumesByCategory(Long categoryId);
}
