package org.example.JobSearch.repository;

import org.example.JobSearch.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository  extends JpaRepository<Vacancy, Long> {
}
