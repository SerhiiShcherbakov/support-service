package com.serhiishcherbakov.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DialogConflictException extends ResponseStatusException {
    public DialogConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
