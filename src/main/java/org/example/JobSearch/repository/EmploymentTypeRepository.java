package org.example.JobSearch.repository;

import org.example.JobSearch.model.EmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploymentTypeRepository extends JpaRepository<EmploymentType, Long> {
    List<EmploymentType> findAllByIdIn(List<Long> ids);
}