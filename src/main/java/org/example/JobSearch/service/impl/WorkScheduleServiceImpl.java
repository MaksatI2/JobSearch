package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.WorkSchedule;
import org.example.JobSearch.repository.WorkScheduleRepository;
import org.example.JobSearch.service.WorkScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkScheduleServiceImpl implements WorkScheduleService {
    private final WorkScheduleRepository workScheduleRepository;

    @Override
    public List<WorkSchedule> findAll() {
        return workScheduleRepository.findAll();
    }

    @Override
    public List<WorkSchedule> findAllById(List<Long> ids) {
        return workScheduleRepository.findAllByIdIn(ids);
    }
}