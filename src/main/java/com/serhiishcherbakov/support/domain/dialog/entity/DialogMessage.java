package com.serhiishcherbakov.support.domain.dialog.entity;

import com.serhiishcherbakov.support.api.request.MessageRequestDto;
import com.serhiishcherbakov.support.security.UserDetailsDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class DialogMessage {
    private String userId;
    private String message;
    private String attachmentKey;
    private Instant createdAt;

    public static DialogMessage newMessage(MessageRequestDto messageRequest, UserDetailsDto userDetails) {
        var message = new DialogMessage();
        message.userId = userDetails.id();
        message.message = messageRequest.message();
        message.attachmentKey = messageRequest.attachmentKey();
        message.createdAt = Instant.now();
        return message;
    }
}
