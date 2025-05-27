package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.EmploymentType;
import org.example.JobSearch.repository.EmploymentTypeRepository;
import org.example.JobSearch.service.EmploymentTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmploymentTypeServiceImpl implements EmploymentTypeService {
    private final EmploymentTypeRepository employmentTypeRepository;

    @Override
    public List<EmploymentType> findAll() {
        return employmentTypeRepository.findAll();
    }

    @Override
    public List<EmploymentType> findAllById(List<Long> ids) {
        return employmentTypeRepository.findAllByIdIn(ids);
    }
}