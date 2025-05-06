package org.example.JobSearch.service;


import org.example.JobSearch.dto.EditDTO.EditResumeDTO;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.create.CreateResumeDTO;
import org.example.JobSearch.model.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ResumeService {
    void createResume(CreateResumeDTO resumeDto, BindingResult bindingResult);
    void updateResume(Long resumeId, EditResumeDTO editresumeDto);
    void deleteResume(Long resumeId);
    void refreshResume(Long resumeId);
    void validateCreateResume(CreateResumeDTO resumeDto, BindingResult bindingResult);
    void validateEducation(List<EducationInfoDTO> educationInfos, BindingResult bindingResult);

    Page<ResumeDTO> getResumesByApplicant(Long applicantId, int page, int size);
    Page<ResumeDTO> getAllResumes(String sort,Pageable pageable);
    Page<ResumeDTO> getResumesWithResponsesByApplicantId(Long applicantId, Pageable pageable);
    ResumeDTO getResumeById(Long id);

    Resume getResumeEntityById(Long id);
}
