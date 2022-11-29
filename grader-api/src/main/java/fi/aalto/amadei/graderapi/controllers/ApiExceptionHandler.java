package fi.aalto.amadei.graderapi.controllers;

import fi.aalto.amadei.graderapi.beans.responses.ErrorBean;
import fi.aalto.amadei.graderapi.exceptions.ElementNotFoundException;
import fi.aalto.amadei.graderapi.exceptions.InvalidDatabaseInteractionException;
import fi.aalto.amadei.graderapi.exceptions.InvalidParameterTypeException;
import fi.aalto.amadei.graderapi.exceptions.UnauthorizedRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorBean handleNoHandlerFoundException(NoHandlerFoundException e) {
        return new ErrorBean(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorBean handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new ErrorBean(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBean handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ErrorBean(HttpStatus.BAD_REQUEST, "Payload invalid or missing");
    }

    @ExceptionHandler(InvalidParameterTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBean handleInvalidParameterTypeException(InvalidParameterTypeException e) {
        return new ErrorBean(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ElementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorBean handleElementNotFoundException(ElementNotFoundException e) {
        return new ErrorBean(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBean handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new ErrorBean(HttpStatus.BAD_REQUEST, "Query parameter " + e.getParameterName() + " is mandatory");
    }

    @ExceptionHandler(UnauthorizedRequestException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorBean handleUnauthorizedRequestException(UnauthorizedRequestException e) {
        return new ErrorBean(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(InvalidDatabaseInteractionException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorBean handleInvalidDatabaseInteractionException(InvalidDatabaseInteractionException e) {
        return new ErrorBean(HttpStatus.BAD_GATEWAY, "Internal error. Failed to interact with database");
    }
}
