package org.example.JobSearch.exceptions.advice;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.exceptions.ErrorResponseBody;
import org.example.JobSearch.service.ErrorService;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final ErrorService errorService;

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseBody> handleNotFoundException(NoSuchElementException e) {
        ErrorResponseBody errorResponse = errorService.makeResponse(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseBody> handleValidationException(MethodArgumentNotValidException e) {
        ErrorResponseBody errorResponse = errorService.makeResponse(e.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseBody> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponseBody errorResponse = errorService.makeResponse(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseBody> handleJsonParseException(HttpMessageNotReadableException e) {
        ErrorResponseBody errorResponse = errorService.makeResponse(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponseBody> handleTypeMismatch(TypeMismatchException ex) {
        ErrorResponseBody errorResponse = errorService.makeResponse(
                new IllegalArgumentException("ID must be a number")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> handleAllOtherExceptions(Exception e) {
        ErrorResponseBody errorResponse = errorService.makeResponse(
                new IllegalArgumentException("Произошла ошибка при обработке запроса. Проверьте входные данные.")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
