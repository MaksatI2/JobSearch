package org.example.JobSearch.service;

import org.example.JobSearch.dto.EducationInfoDTO;

import java.util.List;

public interface EducationInfoService {

    void createEducationInfo(Long resumeId, EducationInfoDTO educationInfoDto);
    void deleteByResumeId(Long resumeId);

    List<EducationInfoDTO> getEducationInfoByResumeId(Long resumeId);


}