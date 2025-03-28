package org.example.JobSearch.exceptions;

import java.util.NoSuchElementException;

public class CategoryNotFoundException extends NoSuchElementException {
  public CategoryNotFoundException(String message) {
    super(message);
  }
}
