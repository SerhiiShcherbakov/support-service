package com.serhiishcherbakov.support.domain.dialog.entity;

import com.serhiishcherbakov.support.api.request.CloseDialogRequestDto;
import com.serhiishcherbakov.support.api.request.MessageRequestDto;
import com.serhiishcherbakov.support.domain.user.User;
import com.serhiishcherbakov.support.exception.DialogConflictException;
import com.serhiishcherbakov.support.exception.DialogForbiddenException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Setter
@Getter

@Document(collection = "dialogs")
@CompoundIndex(
        name = "owner_not_deleted_updated_at_idx",
        def = "{'ownerId': 1, 'deletedAt': 1, 'updatedAt': -1}"
)
public class Dialog {
    @Id
    private String id;
    private DialogStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private String ownerId;
    private String operatorId;
    private List<DialogMessage> messages;
    private DialogFeedback feedback;
    @Version
    private int version;

    public static Dialog newDialog(MessageRequestDto messageRequest, User user) {
        var dialog = new Dialog();
        dialog.status = DialogStatus.NEW;
        dialog.ownerId = user.getId();
        dialog.messages = List.of(DialogMessage.newMessage(messageRequest, user));

        var now = Instant.now();
        dialog.createdAt = now;
        dialog.updatedAt = now;

        return dialog;
    }

    public void addUserMessage(MessageRequestDto messageRequest, User user) {
        validateOwnership(user);
        validateMessagesCount();
        validateNotClosed();

        this.updatedAt = Instant.now();
        this.messages.add(DialogMessage.newMessage(messageRequest, user));
    }

    public void closeByUser(CloseDialogRequestDto closeDialogRequest, User user) {
        validateOwnership(user);

        this.updatedAt = Instant.now();
        this.status = DialogStatus.CLOSED;
        this.feedback = DialogFeedback.newFeedback(closeDialogRequest);
    }

    public void deleteByUser(User user) {
        validateOwnership(user);
        if (this.deletedAt != null) {
            return;
        }

        this.status = DialogStatus.CLOSED;
        var now = Instant.now();
        this.updatedAt = now;
        this.deletedAt = now;
    }

    public void validateOwnership(User user) {
        if (!this.ownerId.equals(user.getId())) {
            throw new DialogForbiddenException("You are not the owner of this dialog");
        }
    }

    private void validateMessagesCount() {
        var userMessagesCount = this.messages.stream()
                .filter(message -> message.getUserId().equals(this.ownerId))
                .count();

        if (userMessagesCount >= 100) {
            throw new DialogConflictException("You have reached the maximum number of messages in the dialog");
        }
    }

    private void validateNotClosed() {
        if (this.status == DialogStatus.CLOSED) {
            throw new DialogConflictException("You can not add messages to a closed dialog");
        }
    }

    public void assignOperator(User user) {
        if (this.status != DialogStatus.NEW) {
            throw new DialogConflictException("You can not assign operator to a dialog that is not in NEW status");
        }

        this.updatedAt = Instant.now();
        this.status = DialogStatus.IN_PROGRESS;
        this.operatorId = user.getId();
    }


    public void addOperatorMessage(MessageRequestDto messageRequest, User user) {
        if (this.operatorId == null || !this.operatorId.equals(user.getId())) {
            throw new DialogForbiddenException("You are not the operator of this dialog");
        }
        validateNotClosed();

        this.updatedAt = Instant.now();
        this.messages.add(DialogMessage.newMessage(messageRequest, user));
    }

    public void closeByOperator(User user) {
        if (this.operatorId == null || !this.operatorId.equals(user.getId())) {
            throw new DialogForbiddenException("You are not the operator of this dialog");
        }

        this.updatedAt = Instant.now();
        this.status = DialogStatus.CLOSED;
    }
}
