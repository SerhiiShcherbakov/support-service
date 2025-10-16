package com.serhiishcherbakov.support.domain.dialog.entity;

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
    private String ownerId;
    private String operatorId;
    private int version;
}
