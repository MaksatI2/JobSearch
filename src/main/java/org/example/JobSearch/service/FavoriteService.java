package org.example.JobSearch.service;

import org.example.JobSearch.dto.VacancyDTO;

import java.util.List;

public interface FavoriteService {
    void addToFavorites(Long userId, Long vacancyId);

    void removeFromFavorites(Long userId, Long vacancyId);

    List<VacancyDTO> getUserFavorites(Long userId);

    List<Long> getFavoriteVacancyIds(Long userId);
}
