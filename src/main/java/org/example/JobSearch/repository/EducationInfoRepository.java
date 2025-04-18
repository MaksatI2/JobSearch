package org.example.JobSearch.repository;

import org.example.JobSearch.model.EducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationInfoRepository extends JpaRepository<EducationInfo, Long> {

    List<EducationInfo> findByResumeId(Long resumeId);

    @Modifying
    @Query("DELETE FROM EducationInfo e WHERE e.resume.id = :resumeId")
    void deleteByResumeId(@Param("resumeId") Long resumeId);
}