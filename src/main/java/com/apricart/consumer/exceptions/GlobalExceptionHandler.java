package com.apricart.consumer.exceptions;

import com.apricart.consumer.generic.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Created on January, 2024
 *
 * @author Kashaf
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RegistrationException.class, BadCredentialsException.class, Exception.class})
    public ResponseEntity<GenericResponse<String>> handleException(Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Error: ";

        if (exception instanceof RegistrationException) {
            status = HttpStatus.BAD_REQUEST;
            message = ((RegistrationException) exception).getErrorMessage();
        } else if (exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            message = exception.getMessage();
        } else if (exception instanceof MethodArgumentNotValidException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Validation Error: " + ((MethodArgumentNotValidException) exception).getBindingResult()
                    .getAllErrors().get(0).getDefaultMessage();
        } else if (exception instanceof CouponValidationException) {
            status = HttpStatus.BAD_REQUEST;
            message = exception.getMessage();
        } else if (exception != null) {
            message = exception.getMessage();
        }


        final GenericResponse<String> response = new GenericResponse<>(status.value(), message, null);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        GenericResponse<String> response = new GenericResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
