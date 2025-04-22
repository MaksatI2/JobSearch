package org.example.JobSearch.service;

import org.example.JobSearch.dto.CategoryDTO;
import org.example.JobSearch.model.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    Category getCategoryById(Long id);
}