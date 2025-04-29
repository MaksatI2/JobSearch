package org.example.JobSearch.repository;

import org.example.JobSearch.model.FavoriteVacancy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteVacancyRepository extends JpaRepository<FavoriteVacancy, Long> {

    @Query("SELECT f FROM FavoriteVacancy f WHERE f.user.id = :userId")
    List<FavoriteVacancy> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(f) FROM FavoriteVacancy f WHERE f.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    Optional<FavoriteVacancy> findByUserIdAndVacancyId(Long userId, Long vacancyId);

    @Modifying
    void deleteByUserIdAndVacancyId(Long userId, Long vacancyId);

    @Query("SELECT f.vacancy.id FROM FavoriteVacancy f WHERE f.user.id = :userId")
    List<Long> findVacancyIdsByUserId(@Param("userId") Long userId);
}