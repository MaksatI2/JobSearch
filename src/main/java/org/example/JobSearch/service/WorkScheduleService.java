package org.example.JobSearch.service;

import org.example.JobSearch.model.WorkSchedule;

import java.util.List;

public interface WorkScheduleService {
    List<WorkSchedule> findAll();
    List<WorkSchedule> findAllById(List<Long> ids);
}