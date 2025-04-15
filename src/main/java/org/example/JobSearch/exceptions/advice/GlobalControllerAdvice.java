package org.example.JobSearch.exceptions.advice;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.exceptions.*;
import org.example.JobSearch.service.ErrorService;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final ErrorService errorService;

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleAccessDenied(AccessDeniedException e) {
        ModelAndView mav = new ModelAndView("errors/403");
        mav.addObject("error", errorService.makeResponse(e));
        return mav;
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            ResumeNotFoundException.class,
            VacancyNotFoundException.class,
            CategoryNotFoundException.class,
            InvalidUserDataException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundException(NoSuchElementException e) {
        ModelAndView mav = new ModelAndView("errors/404");
        mav.addObject("error", errorService.makeResponse(e));
        return mav;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationException(MethodArgumentNotValidException e) {
        ModelAndView mav = new ModelAndView("errors/400");

        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));

        mav.addObject("error", errorService.makeResponse(
                new IllegalArgumentException("Ошибка валидации данных")
        ));
        mav.addObject("fieldErrors", fieldErrors);

        return mav;
    }

    @ExceptionHandler(InvalidRegisterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleInvalidRegisterException(InvalidRegisterException e) {
        ModelAndView mav = new ModelAndView("auth/register");
        mav.addObject("error", errorService.makeResponse(e));
        mav.addObject("type", e.getFieldName().contains("company") ? "employer" : "applicant");
        return mav;
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBadRequestExceptions(Exception e) {
        ModelAndView mav = new ModelAndView("errors/400");
        mav.addObject("error", errorService.makeResponse(e));
        return mav;
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView TypeMismatchException(Exception e) {
        ModelAndView mav = new ModelAndView("errors/400");
        mav.addObject("error", errorService.makeResponse(
                new IllegalArgumentException("ID должно быть числом")
        ));
        return mav;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleAllOtherExceptions(Exception e) {
        ModelAndView mav = new ModelAndView("errors/400");
        mav.addObject("error", errorService.makeResponse(
                new IllegalArgumentException("Произошла ошибка при обработке запроса")
        ));
        return mav;
    }
}