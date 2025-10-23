package com.serhiishcherbakov.support.domain.dialog.entity.projection;

import com.serhiishcherbakov.support.domain.dialog.entity.DialogMessage;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DialogSummary extends DialogProjection {
    private DialogMessage lastMessage;
}
