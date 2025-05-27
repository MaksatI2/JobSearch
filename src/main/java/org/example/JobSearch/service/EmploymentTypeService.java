package org.example.JobSearch.service;

import org.example.JobSearch.model.EmploymentType;

import java.util.List;

public interface EmploymentTypeService {
    List<EmploymentType> findAll();
    List<EmploymentType> findAllById(List<Long> ids);  // Changed to List<Long>
}