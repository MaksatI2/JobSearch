package org.example.JobSearch.repository;

import org.example.JobSearch.model.EducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationInfoRepository extends JpaRepository<EducationInfo, Long> {

    List<EducationInfo> findByResumeId(Long resumeId);

}