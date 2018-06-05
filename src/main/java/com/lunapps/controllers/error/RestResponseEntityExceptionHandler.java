package com.lunapps.controllers.error;

import com.lunapps.exception.DealException;
import com.lunapps.exception.FacebookException;
import com.lunapps.exception.user.AdvertException;
import com.lunapps.exception.user.PhotoException;
import com.lunapps.exception.user.UserEmailAlreadyExistsException;
import com.lunapps.exception.user.UserException;
import com.lunapps.exception.user.UserRegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by olegchorpita on 7/13/17.
 */

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    public RestResponseEntityExceptionHandler() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        BindingResult result = ex.getBindingResult();
        final GenericResponse bodyOfResponse = new GenericResponse(result.getFieldErrors(), result.getGlobalErrors());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final BindingResult result = ex.getBindingResult();
        LOGGER.error("400 Status Code " + result.getFieldError().getDefaultMessage());
        final GenericResponse bodyOfResponse = new GenericResponse(result.getFieldError().getDefaultMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyOfResponse);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    //Custom exceptions
    @ExceptionHandler(value = {UserEmailAlreadyExistsException.class})
    protected ResponseEntity<Object> handleUserEmailAlreadyExist(final RuntimeException ex, final WebRequest request) {
        LOGGER.error("400 Status Code " + ex.getMessage());
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), ex.getClass().getSimpleName());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {FacebookException.class})
    protected ResponseEntity<Object> handleFaceBookException(final RuntimeException ex, final WebRequest request) {
        LOGGER.error("401 Status Code " + ex.getMessage());
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), ex.getClass().getSimpleName());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = {UserRegistrationException.class})
    protected ResponseEntity<Object> handleRegException(final RuntimeException ex, final WebRequest request, final UserRegistrationException e) {
        LOGGER.error("404 Status Code " + ex.getMessage());
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), ex.getClass().getSimpleName(), e.getCode());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {PhotoException.class})
    protected ResponseEntity<Object> handlePhotoException(final RuntimeException ex, final WebRequest request, final PhotoException e) {
        LOGGER.error("404 Status Code " + ex.getMessage());
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), ex.getClass().getSimpleName(), e.getCode());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {UserException.class})
    //todo ask Den how i can generalize this method
    protected ResponseEntity<Object> handleUserException(final RuntimeException ex, final WebRequest request, final UserException e) {
        LOGGER.error("404 Status Code " + ex.getMessage());
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), ex.getClass().getSimpleName(), e.getCode());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {AdvertException.class})
    //todo ask Den how i can generalize this method
    protected ResponseEntity<Object> handleAdvertException(final RuntimeException ex, final WebRequest request, final AdvertException e) {
        LOGGER.error("404 Status Code " + ex.getMessage());
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), ex.getClass().getSimpleName(), e.getSum());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {DealException.class})
    //todo ask Den how i can generalize this method
    protected ResponseEntity<Object> handleDealException(final RuntimeException ex, final WebRequest request, final DealException e) {
        LOGGER.error("404 Status Code " + ex.getMessage());
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), ex.getClass().getSimpleName(), e.getCode());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}