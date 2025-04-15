package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.EducationInfoDao;
import org.example.JobSearch.dto.EducationInfoDTO;
import org.example.JobSearch.exceptions.CreateResumeException;
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
        validateCreateEducationInfo(resumeId, educationInfoDto);

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

    @Override
    public void updateEducationInfo(Long id, EducationInfoDTO educationInfoDto) {

        EducationInfo educationInfo = new EducationInfo();
        educationInfo.setId(id);
        educationInfo.setInstitution(educationInfoDto.getInstitution());
        educationInfo.setProgram(educationInfoDto.getProgram());
        educationInfo.setStartDate(educationInfoDto.getStartDate());
        educationInfo.setEndDate(educationInfoDto.getEndDate());
        educationInfo.setDegree(educationInfoDto.getDegree());

        educationInfoDao.updateEducationInfo(id, educationInfo);
    }

    @Override
    public void deleteEducationInfo(Long id) {
        educationInfoDao.deleteEducationInfo(id);
    }

    @Override
    public void validateCreateEducationInfo(Long resumeId, EducationInfoDTO educationInfoDto) {

        if (educationInfoDto.getInstitution() == null || educationInfoDto.getInstitution().trim().isEmpty()) {
            throw new CreateResumeException("institution", "Название учебного заведения не может быть пустым");
        }

        if (educationInfoDto.getInstitution().length() < 2) {
            throw new CreateResumeException("institution", "Название учебного заведения должно содержать минимум 2 символа");
        }

        if (educationInfoDto.getProgram() == null || educationInfoDto.getProgram().trim().isEmpty()) {
            throw new CreateResumeException("program", "Программа обучения не может быть пустой");
        }

        if (educationInfoDto.getDegree() == null || educationInfoDto.getDegree().trim().isEmpty()) {
            throw new CreateResumeException("degree", "Степень не может быть пустой");
        }

        if (educationInfoDto.getStartDate() == null) {
            throw new CreateResumeException("startDate", "Дата начала обучения не может быть пустой");
        }

        if (educationInfoDto.getEndDate() != null && educationInfoDto.getStartDate() != null) {
            if (educationInfoDto.getEndDate().before(educationInfoDto.getStartDate())) {
                throw new CreateResumeException("endDate", "Дата окончания не может быть раньше даты начала");
            }
        }
    }
}