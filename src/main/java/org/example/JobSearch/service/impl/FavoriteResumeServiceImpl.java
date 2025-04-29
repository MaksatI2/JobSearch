package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.model.*;
import org.example.JobSearch.repository.FavoriteResumeRepository;
import org.example.JobSearch.service.FavoriteResumeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteResumeServiceImpl implements FavoriteResumeService {
    private final FavoriteResumeRepository resumeRepository;

    @Override
    public void addResumeToFavorites(Long userId, Long resumeId) {
        if (resumeRepository.findByUserIdAndResumeId(userId, resumeId).isEmpty()) {
            FavoriteResume fav = FavoriteResume.builder()
                    .user(User.builder().id(userId).build())
                    .resume(Resume.builder().id(resumeId).build())
                    .build();
            resumeRepository.save(fav);
        }
    }

    @Override
    public List<ResumeDTO> getUserFavoriteResumes(Long userId) {
        return resumeRepository.findByUserId(userId).stream()
                .map(favorite -> convertToResumeDTO(favorite.getResume()))
                .toList();
    }

    @Transactional
    @Override
    public void removeResumeFromFavorites(Long userId, Long resumeId) {
        resumeRepository.deleteByUserIdAndResumeId(userId, resumeId);
    }

    @Override
    public List<Long> getFavoriteResumeIds(Long userId) {
        return resumeRepository.findResumeIdsByUserId(userId);
    }

    private ResumeDTO convertToResumeDTO(Resume resume) {
        return ResumeDTO.builder()
                .id(resume.getId())
                .applicantId(resume.getApplicant().getId())
                .applicantName(resume.getApplicant().getName())
                .applicantAge(resume.getApplicant().getAge())
                .applicantAvatar(resume.getApplicant().getAvatar())
                .categoryId(resume.getCategory().getId())
                .name(resume.getName())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .createDate(resume.getCreateDate())
                .updateTime(resume.getUpdateTime())
                .build();
    }
}