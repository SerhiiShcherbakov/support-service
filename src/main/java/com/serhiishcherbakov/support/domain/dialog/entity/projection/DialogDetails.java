package com.serhiishcherbakov.support.domain.dialog.entity.projection;

import com.serhiishcherbakov.support.domain.dialog.entity.DialogFeedback;
import com.serhiishcherbakov.support.domain.dialog.entity.DialogMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DialogDetails extends DialogSummary {
    private List<DialogMessage> messages;
    private DialogFeedback feedback;
}
