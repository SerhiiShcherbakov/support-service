package com.serhiishcherbakov.support.domain.dialog.entity.projection;

import com.serhiishcherbakov.support.domain.dialog.entity.DialogStatus;
import com.serhiishcherbakov.support.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class DialogProjection {
    private String id;
    private DialogStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private User owner;
    private User operator;
    private int version;
}
