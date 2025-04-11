package org.example.JobSearch.exceptions;

import java.util.NoSuchElementException;

public class InvalidRegisterException extends NoSuchElementException {
    private final String fieldName;
    private final String message;

    public InvalidRegisterException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
