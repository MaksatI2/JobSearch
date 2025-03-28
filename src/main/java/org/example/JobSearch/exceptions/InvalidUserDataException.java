package org.example.JobSearch.exceptions;

import java.util.NoSuchElementException;

public class InvalidUserDataException extends NoSuchElementException {
    public InvalidUserDataException(String message) {
        super(message);
    }
}
