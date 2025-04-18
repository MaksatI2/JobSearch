package org.example.JobSearch.exceptions;

public class CreateVacancyException extends RuntimeException {
  private final String fieldName;
  private final String message;

  public CreateVacancyException(String fieldName, String message) {
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
