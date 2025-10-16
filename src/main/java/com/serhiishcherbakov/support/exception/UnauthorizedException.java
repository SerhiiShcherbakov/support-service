package com.serhiishcherbakov.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedException extends ResponseStatusException {
    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
