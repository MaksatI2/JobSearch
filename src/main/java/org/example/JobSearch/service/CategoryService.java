package org.example.JobSearch.service;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.Category;
import org.example.JobSearch.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public boolean existsById(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }
}