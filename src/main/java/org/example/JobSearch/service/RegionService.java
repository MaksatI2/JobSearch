package org.example.JobSearch.service;

import org.example.JobSearch.model.Region;

import java.util.List;
import java.util.Optional;

public interface RegionService {
    Optional<Region> findById(Long id);
    List<Region> findAll();
}