package org.example.JobSearch.repository;

import org.example.JobSearch.model.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
    List<ContactInfo> findByResumeId(Long resumeId);

    @Modifying
    @Query("DELETE FROM ContactInfo e WHERE e.resume.id = :resumeId")
    void deleteByResumeId(@Param("resumeId") Long resumeId);
}