package com.serhiishcherbakov.support.domain.dialog.entity;

import com.serhiishcherbakov.support.api.request.CloseDialogRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class DialogFeedback {
    private Integer score;
    private String message;
    private Instant createdAt;

    public static DialogFeedback newFeedback(CloseDialogRequestDto closeDialogRequest) {
        var feedback = new DialogFeedback();
        feedback.score = closeDialogRequest.score();
        feedback.message = closeDialogRequest.message();
        feedback.createdAt = Instant.now();
        return feedback;
    }
}
