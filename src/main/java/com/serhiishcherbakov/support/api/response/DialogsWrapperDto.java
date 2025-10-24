package com.serhiishcherbakov.support.api.response;

import com.serhiishcherbakov.support.domain.dialog.entity.DialogSummary;

import java.util.List;

public record DialogsWrapperDto(List<DialogSummary> dialogs) {
}
