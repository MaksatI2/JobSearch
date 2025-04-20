package org.example.JobSearch.repository;

import org.example.JobSearch.model.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
    List<ContactInfo> findByResumeId(Long resumeId);
}