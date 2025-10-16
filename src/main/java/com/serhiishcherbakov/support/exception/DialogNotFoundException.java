package com.serhiishcherbakov.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DialogNotFoundException extends ResponseStatusException {
    public DialogNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Dialog not found");
    }
}
