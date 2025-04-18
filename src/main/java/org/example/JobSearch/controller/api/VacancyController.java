package org.example.JobSearch.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.EditDTO.EditVacancyDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.service.VacancyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vacancies")
@RequiredArgsConstructor
public class VacancyController {
    private final VacancyService vacancyService;


//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateVacancy(@PathVariable Long id, @RequestBody @Valid EditVacancyDTO editvacancyDTO) {
//        vacancyService.updateVacancy(id, editvacancyDTO);
//        return ResponseEntity.ok("Vacancy updated successfully");
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVacancy(@PathVariable @Valid Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.ok("Vacancy deleted successfully");
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getVacanciesByCategory(@PathVariable @Valid Long categoryId) {
            List<VacancyDTO> vacancies = vacancyService.getVacanciesByCategory(categoryId);
            return ResponseEntity.ok(vacancies);
    }

    @GetMapping("/ApplToVacancy/{id}")
    public ResponseEntity<?> getRespApplToVacancy(@PathVariable @Valid Long id) {
            List<VacancyDTO> vacancies = vacancyService.getRespApplToVacancy(id);
            return ResponseEntity.ok(vacancies);
    }
}