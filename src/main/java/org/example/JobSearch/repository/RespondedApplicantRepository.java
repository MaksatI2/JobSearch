package org.example.JobSearch.repository;

import org.example.JobSearch.model.RespondedApplicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespondedApplicantRepository extends JpaRepository<RespondedApplicant, Long> {
}