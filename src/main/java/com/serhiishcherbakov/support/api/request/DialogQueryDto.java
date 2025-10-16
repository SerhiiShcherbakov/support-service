package com.serhiishcherbakov.support.api.request;

import com.serhiishcherbakov.support.domain.dialog.entity.DialogStatus;

public record DialogQueryDto(DialogStatus status,
                             String ownerId,
                             String operatorId) {
}
