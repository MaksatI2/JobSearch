package org.example.JobSearch.repository;

import org.example.JobSearch.model.FavoriteResume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FavoriteResumeRepository extends JpaRepository<FavoriteResume, Long> {
    List<FavoriteResume> findByUserId(Long userId);
    Optional<FavoriteResume> findByUserIdAndResumeId(Long userId, Long resumeId);

    @Transactional
    void deleteByUserIdAndResumeId(Long userId, Long resumeId);

    @Query("SELECT f.resume.id FROM FavoriteResume f WHERE f.user.id = :userId")
    List<Long> findResumeIdsByUserId(Long userId);
}