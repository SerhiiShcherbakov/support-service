package com.serhiishcherbakov.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Getter
public class ValidationException extends ResponseStatusException {
    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "Validation error");
        this.errors = errors;
    }
}
