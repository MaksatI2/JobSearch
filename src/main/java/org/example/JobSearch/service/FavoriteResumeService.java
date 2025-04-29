package org.example.JobSearch.service;

import org.example.JobSearch.dto.ResumeDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FavoriteResumeService {
    void addResumeToFavorites(Long userId, Long resumeId);

    List<ResumeDTO> getUserFavoriteResumes(Long userId);

    @Transactional
    void removeResumeFromFavorites(Long userId, Long resumeId);

    List<Long> getFavoriteResumeIds(Long userId);
}
