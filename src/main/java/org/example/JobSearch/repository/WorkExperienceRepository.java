package org.example.JobSearch.repository;

import org.example.JobSearch.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {

    List<WorkExperience> findByResumeId(Long resumeId);

    @Modifying
    @Query("DELETE FROM WorkExperience w WHERE w.resume.id = :resumeId")
    void deleteByResumeId(@Param("resumeId") Long resumeId);
}