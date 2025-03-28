package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.EducationInfoDao;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.exceptions.InvalidUserDataException;
import org.example.JobSearch.model.EducationInfo;
import org.example.JobSearch.service.EducationInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationInfoServiceImpl implements EducationInfoService {
    private final EducationInfoDao educationInfoDao;

    @Override
    public void createEducationInfo(Long resumeId, EducationInfoDTO educationInfoDto) {
        if (educationInfoDto.getId() != null) {
            throw new InvalidUserDataException("ID не должен быть указан при создании");
        }

        EducationInfo educationInfo = new EducationInfo();
        educationInfo.setInstitution(educationInfoDto.getInstitution());
        educationInfo.setProgram(educationInfoDto.getProgram());
        educationInfo.setStartDate(educationInfoDto.getStartDate());
        educationInfo.setEndDate(educationInfoDto.getEndDate());
        educationInfo.setDegree(educationInfoDto.getDegree());

        educationInfoDao.createEducationInfo(resumeId, educationInfo);
    }

    @Override
    public List<EducationInfoDTO> getEducationInfoByResumeId(Long resumeId) {
        return educationInfoDao.getEducationInfoByResumeId(resumeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private EducationInfoDTO toDTO(EducationInfo educationInfo) {
        return EducationInfoDTO.builder()
                .id(educationInfo.getId())
                .resumeId(educationInfo.getResumeId())
                .institution(educationInfo.getInstitution())
                .program(educationInfo.getProgram())
                .startDate(educationInfo.getStartDate())
                .endDate(educationInfo.getEndDate())
                .degree(educationInfo.getDegree())
                .build();
    }
}