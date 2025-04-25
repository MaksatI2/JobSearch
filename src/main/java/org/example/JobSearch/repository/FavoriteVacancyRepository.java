package org.example.JobSearch.repository;

import org.example.JobSearch.model.FavoriteVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteVacancyRepository extends JpaRepository<FavoriteVacancy, Long> {
    List<FavoriteVacancy> findByUserId(Long userId);
    Optional<FavoriteVacancy> findByUserIdAndVacancyId(Long userId, Long vacancyId);
    void deleteByUserIdAndVacancyId(Long userId, Long vacancyId);
}