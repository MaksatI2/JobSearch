package org.example.JobSearch.service.impl;

import org.example.JobSearch.exceptions.ErrorResponseBody;
import org.example.JobSearch.service.ErrorService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErrorServiceImpl implements ErrorService {

    @Override
    public ErrorResponseBody makeResponse(Exception ex) {
        Map<String, List> reasons = new HashMap<>();
        reasons.put("errors", List.of(ex.getMessage()));

        return ErrorResponseBody.builder()
                .title("Error occurred")
                .reasons(reasons)
                .build();
    }


    @Override
    public ErrorResponseBody makeResponse(BindingResult bindingResult) {
        Map<String, List> reasons = new HashMap<>();
        bindingResult.getFieldErrors().stream()
                .filter(err -> err.getDefaultMessage() != null)
                .forEach(err -> {
                    List<String> errors = new ArrayList<>();
                    errors.add(err.getDefaultMessage());
                    if (reasons.containsKey(err.getField())) {
                        reasons.put(err.getField(), errors);
                    }
                });
        return ErrorResponseBody.builder()
                .title("Validation Error")
                .reasons(reasons)
                .build();

    }
}
