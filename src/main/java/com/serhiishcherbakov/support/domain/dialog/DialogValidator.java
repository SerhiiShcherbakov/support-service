package com.serhiishcherbakov.support.domain.dialog;

import com.serhiishcherbakov.support.api.request.CloseDialogRequestDto;
import com.serhiishcherbakov.support.api.request.MessageRequestDto;
import com.serhiishcherbakov.support.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class DialogValidator {
    private static final int MAX_MESSAGE_LENGTH = 300;
    private static final int MAX_ATTACHMENT_KEY_LENGTH = 100;

    public void validateDialogMessage(MessageRequestDto messageRequest) {
        var errors = new HashMap<String, String>();

        if (isBlank(messageRequest.message())) {
            errors.put("message", "Message can not be empty");
        }

        if (isLengthGreaterThan(messageRequest.message(), MAX_MESSAGE_LENGTH)) {
            errors.put("message", String.format("Message can not be longer than %s characters", MAX_MESSAGE_LENGTH));
        }

        if (isLengthGreaterThan(messageRequest.attachmentKey(), MAX_ATTACHMENT_KEY_LENGTH)) {
            errors.put("attachmentKey", String.format("Attachment key can not be longer than %s characters", MAX_ATTACHMENT_KEY_LENGTH));
        }

        throwIfHasErrors(errors);
    }

    public void validateCloseDialogRequest(CloseDialogRequestDto closeDialogRequest) {
        var errors = new HashMap<String, String>();

        if (isNotBlank(closeDialogRequest.message()) && closeDialogRequest.score() == null) {
            errors.put("score", "Score can not be empty if message is present");
        }

        if (closeDialogRequest.score() != null && isNotBetween(closeDialogRequest.score(), 1, 5)) {
            errors.put("score", "Score must be between 1 and 5");
        }

        if (isLengthGreaterThan(closeDialogRequest.message(), MAX_MESSAGE_LENGTH)) {
            errors.put("message", String.format("Message can not be longer than %s characters", MAX_MESSAGE_LENGTH));
        }

        throwIfHasErrors(errors);
    }

    private boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private boolean isLengthGreaterThan(String s, int length) {
        return s != null && s.length() > length;
    }

    private boolean isNotBetween(int value, int min, int max) {
        return value < min || value > max;
    }

    private void throwIfHasErrors(HashMap<String, String> errors) {
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
