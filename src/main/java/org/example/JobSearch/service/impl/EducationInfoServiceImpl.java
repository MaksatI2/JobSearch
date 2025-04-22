package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.model.EducationInfo;
import org.example.JobSearch.model.Resume;
import org.example.JobSearch.repository.EducationInfoRepository;
import org.example.JobSearch.service.EducationInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationInfoServiceImpl implements EducationInfoService {
    private final EducationInfoRepository educationInfoRepository;

    @Override
    @Transactional
    public void createEducationInfo(Long resumeId, EducationInfoDTO educationInfoDto) {
        EducationInfo educationInfo = new EducationInfo();

        Resume resume = new Resume();
        resume.setId(resumeId);
        educationInfo.setResume(resume);
        educationInfo.setInstitution(educationInfoDto.getInstitution());
        educationInfo.setProgram(educationInfoDto.getProgram());
        educationInfo.setStartDate(educationInfoDto.getStartDate());
        educationInfo.setEndDate(educationInfoDto.getEndDate());
        educationInfo.setDegree(educationInfoDto.getDegree());

        educationInfoRepository.save(educationInfo);
    }

    @Override
    @Transactional
    public void deleteByResumeId(Long resumeId) {
        educationInfoRepository.deleteByResumeId(resumeId);
    }

    @Override
    public List<EducationInfoDTO> getEducationInfoByResumeId(Long resumeId) {
        return educationInfoRepository.findByResumeId(resumeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private EducationInfoDTO toDTO(EducationInfo educationInfo) {
        return EducationInfoDTO.builder()
                .id(educationInfo.getId())
                .resumeId(educationInfo.getResume().getId())
                .institution(educationInfo.getInstitution())
                .program(educationInfo.getProgram())
                .startDate(educationInfo.getStartDate())
                .endDate(educationInfo.getEndDate())
                .degree(educationInfo.getDegree())
                .build();
    }
}