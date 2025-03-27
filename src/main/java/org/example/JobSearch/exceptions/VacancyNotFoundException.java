package org.example.JobSearch.exceptions;

import java.util.NoSuchElementException;

public class VacancyNotFoundException extends NoSuchElementException {
    public VacancyNotFoundException() {
    }

    public VacancyNotFoundException(String message) {
        super(message);
    }


}