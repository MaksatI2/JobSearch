package org.example.JobSearch.service;

import org.example.JobSearch.exceptions.ErrorResponseBody;
import org.springframework.validation.BindingResult;


public interface ErrorService {
    ErrorResponseBody makeResponse(Exception exception);
}
