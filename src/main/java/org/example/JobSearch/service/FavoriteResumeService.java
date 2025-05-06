package org.example.JobSearch.service;

import org.example.JobSearch.dto.ResumeDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FavoriteResumeService {
    @Transactional
    void removeResumeFromFavorites(Long userId, Long resumeId);
    void addResumeToFavorites(Long userId, Long resumeId);

    List<ResumeDTO> getUserFavoriteResumes(Long userId);
    List<Long> getFavoriteResumeIds(Long userId);
}
