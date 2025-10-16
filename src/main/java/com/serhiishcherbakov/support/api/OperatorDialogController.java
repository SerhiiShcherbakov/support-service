package com.serhiishcherbakov.support.api;

import com.serhiishcherbakov.support.domain.dialog.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operator/dialogs")
@RequiredArgsConstructor
public class OperatorDialogController {
    private final DialogService dialogService;
}
