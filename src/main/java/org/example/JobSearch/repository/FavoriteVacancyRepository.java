package org.example.JobSearch.repository;

import org.example.JobSearch.model.FavoriteVacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FavoriteVacancyRepository extends JpaRepository<FavoriteVacancy, Long> {
    List<FavoriteVacancy> findByUserId(Long userId);

    Optional<FavoriteVacancy> findByUserIdAndVacancyId(Long userId, Long vacancyId);

    @Transactional
    void deleteByUserIdAndVacancyId(Long userId, Long vacancyId);

    @Query("SELECT f.vacancy.id FROM FavoriteVacancy f WHERE f.user.id = :userId")
    List<Long> findVacancyIdsByUserId(Long userId);
}