package org.example.JobSearch.service;

import org.example.JobSearch.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
}