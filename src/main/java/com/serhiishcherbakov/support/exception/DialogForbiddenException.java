package com.serhiishcherbakov.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DialogForbiddenException extends ResponseStatusException {
    public DialogForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
