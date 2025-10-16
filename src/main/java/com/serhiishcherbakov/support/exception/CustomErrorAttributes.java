package com.serhiishcherbakov.support.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest request, ErrorAttributeOptions options) {
        Map<String, Object> attributes = super.getErrorAttributes(request, options);

        Throwable error = getError(request);
        if (error instanceof ValidationException validationException && validationException.getErrors() != null) {
            attributes.put("errors", validationException.getErrors());
        }
        return attributes;
    }
}
