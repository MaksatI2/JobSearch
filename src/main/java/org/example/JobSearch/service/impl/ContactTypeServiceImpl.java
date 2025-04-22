package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.ContactType;
import org.example.JobSearch.repository.ContactTypeRepository;
import org.example.JobSearch.service.ContactTypeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactTypeServiceImpl implements ContactTypeService {
    private final ContactTypeRepository contactTypeRepository;

    @Override
    public ContactType getById(Long id) {
        return contactTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID типа контакта: " + id));
    }
}
