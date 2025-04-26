package org.example.JobSearch.service;

import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.VacancyDTO;

import java.util.List;

public interface FavoriteService {
    void addVacancyToFavorites(Long userId, Long vacancyId);
    List<VacancyDTO> getUserFavoriteVacancies(Long userId);
    void removeVacancyFromFavorites(Long userId, Long vacancyId);
    List<Long> getFavoriteVacancyIds(Long userId);

    void addResumeToFavorites(Long userId, Long resumeId);
    List<ResumeDTO> getUserFavoriteResumes(Long userId);
    void removeResumeFromFavorites(Long userId, Long resumeId);
    List<Long> getFavoriteResumeIds(Long userId);
}