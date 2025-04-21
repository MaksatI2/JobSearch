package org.example.JobSearch.controller.api;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.RespondedApplicantDTO;
import org.example.JobSearch.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/response")
@RequiredArgsConstructor
public class ResponseController {
    private final ApplicationService applicationService;

//    @PostMapping("/{vacancyId}")
//    public ResponseEntity<String> responseToVacancy(@PathVariable("vacancyId") Long vacancyId, @RequestBody RespondedApplicantDTO respondedApplicantDTO) {
//        applicationService.respondToVacancy(vacancyId,respondedApplicantDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Response submitted successfully");
//    }
}
