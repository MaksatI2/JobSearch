package org.example.JobSearch.service.impl;

import org.example.JobSearch.exceptions.ErrorResponseBody;
import org.example.JobSearch.service.ErrorService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErrorServiceImpl implements ErrorService {

    @Override
    public ErrorResponseBody makeResponse(Exception ex) {
        Map<String, List> reasons = new HashMap<>();
        reasons.put("ошибки", List.of(ex.getMessage()));

        return ErrorResponseBody.builder()
                .title("Неверный запрос")
                .detail(ex.getClass().getSimpleName())
                .reasons(reasons)
                .timestamp(Instant.now().toString())
                .build();
    }
}
