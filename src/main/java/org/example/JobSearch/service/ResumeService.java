package org.example.JobSearch.service;


import jakarta.validation.Valid;
import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.model.Resume;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ResumeService {
    List<ResumeDTO> getResumesByApplicant(Long applicantId);

    void createResume(CreateResumeDTO resumeDto, BindingResult bindingResult);
    void updateResume(Long resumeId, EditResumeDTO editresumeDto);

    void deleteResume(Long resumeId);

    void refreshResume(Long resumeId);

    List<ResumeDTO> getAllResumes();

    List<ResumeDTO> getUserResumes(Long applicants_id);

    List<ResumeDTO> getResumesByCategory(Long categoryId);

    void validateCreateResume(CreateResumeDTO resumeDto, BindingResult bindingResult);

    ResumeDTO getResumeById(Long id);

    void validateEducation(List<EducationInfoDTO> educationInfos, BindingResult bindingResult);
}
