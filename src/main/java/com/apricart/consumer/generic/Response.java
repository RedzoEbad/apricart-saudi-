package com.apricart.consumer.generic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import static com.apricart.consumer.security.constants.ResponseMessage.*;

public class Response<C> {

    public static <T> ResponseEntity<GenericResponse<T>> create(HttpStatus httpStatus, String message, T data) {
        return new ResponseEntity<>(new GenericResponse<T>(httpStatus.value(), message, data), new LinkedMultiValueMap<>(), httpStatus);
    }

    //    ---------- Success ------------//
    public static <T> ResponseEntity<GenericResponse<T>> success(T data) {
        return create(HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), data);
    }

    public static ResponseEntity<GenericResponse<Void>> success() {
        return create(HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), null);
    }

    public static <T> ResponseEntity<GenericResponse<T>> success(String message,T data) {
        return create(HttpStatus.OK,message, data);
    }

    //    ---------- Error ------------

    public static <T> ResponseEntity<GenericResponse<T>> error() {
        return create(HttpStatus.BAD_REQUEST, ERROR_FAILED, null);
    }

    public static <T> ResponseEntity<GenericResponse<T>> error(T data) {
        return create(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), data);
    }

    public static <T> ResponseEntity<GenericResponse<T>> error(String errorMessage) {
        return create(HttpStatus.BAD_REQUEST, errorMessage, null);
    }

    public static <T> ResponseEntity<GenericResponse<T>> error(String errorMessage, T data) {
        return create(HttpStatus.BAD_REQUEST, errorMessage, data);
    }

    //    ---------- Not Found ------------

    public static <T> ResponseEntity<GenericResponse<T>> notFound() {
        return create(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), null);
    }

    public static <T> ResponseEntity<GenericResponse<T>> notFound(String errorMessage) {
        return create(HttpStatus.NOT_FOUND, errorMessage, null);
    }
    public static <T> ResponseEntity<GenericResponse<T>> notFound(String errorMessage, T data) {
        return create(HttpStatus.NOT_FOUND, errorMessage, data);
    }

    //    ---------- Others ------------

    public static <T> ResponseEntity<GenericResponse<T>> created(String message) {
        return create(HttpStatus.CREATED, message , null);
    }

    public static <T> ResponseEntity<GenericResponse<T>> created() {
        return create(HttpStatus.CREATED, HttpStatus.CREATED.getReasonPhrase(), null);
    }
    public static <T> ResponseEntity<GenericResponse<T>> created(T data) {
        return create(HttpStatus.CREATED, HttpStatus.CREATED.getReasonPhrase(), data);
    }
    public static <T> ResponseEntity<GenericResponse<T>> unauthorized(String errorMessage) {
        return create(HttpStatus.UNAUTHORIZED, errorMessage,null);
    }
    public static <T> ResponseEntity<GenericResponse<T>> notAcceptable(String errorMessage) {
        return create(HttpStatus.NOT_ACCEPTABLE, errorMessage,null);
    }
    public static <T> ResponseEntity<GenericResponse<T>> customResponse(HttpStatus status, String errorMessage, T data) {
        return create(status, errorMessage, data);
    }

    public static <T> ResponseEntity<GenericResponse<T>> customResponse(HttpStatus status, String errorMessage) {
        return create(status, errorMessage,null);
    }


}
