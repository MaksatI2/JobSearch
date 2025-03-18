package org.example.JobSearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.RespondedApplicantDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.service.ApplicationService;
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
    private final ApplicationService applicationService;

    @PostMapping("/createVacancy")
    public ResponseEntity<String> createVacancy(@RequestBody VacancyDTO vacancyDTO) {
        vacancyService.createVacancy(vacancyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Vacancy created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVacancy(@PathVariable Integer id, @RequestBody VacancyDTO vacancyDTO) {
        vacancyService.updateVacancy(id, vacancyDTO);
        return ResponseEntity.ok("Vacancy updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVacancy(@PathVariable Integer id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.ok("Vacancy deleted successfully");
    }

    @GetMapping("/vacancies")
    public List<VacancyDTO> getAllVacancies() {
        return vacancyService.getAllVacancies();
    }

    @GetMapping("/vacancies/category/{categoryId}")
    public List<VacancyDTO> getVacanciesByCategory(@PathVariable Integer categoryId) {
        return vacancyService.getVacanciesByCategory(categoryId);
    }

    @PostMapping("/response/{vacancyId}")
    public ResponseEntity<String> responseToVacancy(@PathVariable("vacancyId") Integer vacancyId,@RequestBody RespondedApplicantDTO respondedApplicantDTO) {
        applicationService.respondToVacancy(vacancyId,respondedApplicantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Response submitted successfully");
    }

    @GetMapping("/applicatons")
    public ResponseEntity<String> getApplicantsForVacancy(Integer vacancyId){
        vacancyService.getApplicantsVacancy(vacancyId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Applicants successfully found");
    }
}