package org.example.JobSearch.service;

import org.example.JobSearch.dto.VacancyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FavoriteVacancyService {
    @Transactional
    void removeVacancyFromFavorites(Long userId, Long vacancyId);
    void addVacancyToFavorites(Long userId, Long vacancyId);

    Page<VacancyDTO> getUserFavoriteVacancies(Long userId, Pageable pageable);
    List<Long> getFavoriteVacancyIds(Long userId);
}
