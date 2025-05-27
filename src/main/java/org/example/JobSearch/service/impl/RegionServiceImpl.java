package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.Region;
import org.example.JobSearch.repository.RegionRepository;
import org.example.JobSearch.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;

    @Override
    public Optional<Region> findById(Long id) {
        return regionRepository.findById(id);
    }

    @Override
    public List<Region> findAll() {
        return regionRepository.findAll();
    }
}