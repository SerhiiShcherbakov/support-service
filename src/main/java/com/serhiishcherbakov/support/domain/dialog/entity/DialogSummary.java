package com.serhiishcherbakov.support.domain.dialog.entity;

import com.serhiishcherbakov.support.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class DialogSummary {
    private String id;
    private DialogStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private User owner;
    private User operator;
    private int version;
    private DialogMessage lastMessage;
}
