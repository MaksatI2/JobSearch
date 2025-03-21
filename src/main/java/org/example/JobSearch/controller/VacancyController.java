package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.exceptions.VacancyNotFoundException;
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
    public ResponseEntity<?> getAllVacancies() {
        try {
            List<VacancyDTO> vacancies = vacancyService.getAllVacancies();
            return ResponseEntity.ok(vacancies);
        } catch (VacancyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/vacancies/category/{categoryId}")
    public ResponseEntity<?> getVacanciesByCategory(@PathVariable Long categoryId) {
        try {
            List<VacancyDTO> vacancies = vacancyService.getVacanciesByCategory(categoryId);
            return ResponseEntity.ok(vacancies);
        } catch (VacancyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/respApplToVacancy/{id}")
    public ResponseEntity<?> getRespApplToVacancy(@PathVariable Long id) {
        try {
            List<VacancyDTO> vacancies = vacancyService.getRespApplToVacancy(id);
            return ResponseEntity.ok(vacancies);
        } catch (VacancyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}