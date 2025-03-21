package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.service.VacancyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacancy")
@RequiredArgsConstructor
public class VacancyController {
    private final VacancyService vacancyService;

    @PostMapping
    public ResponseEntity<String> createVacancy(@RequestBody VacancyDTO vacancyDTO) {
        vacancyService.createVacancy(vacancyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Vacancy created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVacancy(@PathVariable Long id, @RequestBody VacancyDTO vacancyDTO) {
        vacancyService.updateVacancy(id, vacancyDTO);
        return ResponseEntity.ok("Vacancy updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.ok("Vacancy deleted successfully");
    }

    @GetMapping("/allVacancies")
    public List<VacancyDTO> getAllVacancies() {
        return vacancyService.getAllVacancies();
    }

    @GetMapping("/vacancies/category/{categoryId}")
    public List<VacancyDTO> getVacanciesByCategory(@PathVariable Long categoryId) {
        return vacancyService.getVacanciesByCategory(categoryId);
    }

}