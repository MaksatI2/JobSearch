package org.example.JobSearch.service;


import jakarta.validation.Valid;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;

import java.util.List;

public interface ResumeService {
    List<ResumeDTO> getResumesByApplicant(Long applicantId);

    void createResume(CreateResumeDTO resumeDto);
    void updateResume(Long resumeId, EditResumeDTO editresumeDto);
    void deleteResume(Long resumeId);

    List<ResumeDTO> getAllResumes();

    List<ResumeDTO> getUserResumes(Long applicants_id);

    List<ResumeDTO> getResumesByCategory(Long categoryId);

    void validateCreateResume(CreateResumeDTO resumeDTO);

    EditResumeDTO convertToEditDTO(ResumeDTO resume);

    ResumeDTO getResumeById(Long id);
}
