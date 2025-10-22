package com.serhiishcherbakov.support.api;

import com.serhiishcherbakov.support.api.request.DialogQueryDto;
import com.serhiishcherbakov.support.api.request.MessageRequestDto;
import com.serhiishcherbakov.support.api.response.DialogWrapperDto;
import com.serhiishcherbakov.support.api.response.DialogsWrapperDto;
import com.serhiishcherbakov.support.domain.dialog.OperatorDialogService;
import com.serhiishcherbakov.support.security.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operator/dialogs")
@RequiredArgsConstructor
public class OperatorDialogController {
    private final OperatorDialogService dialogService;

    @GetMapping
    public DialogsWrapperDto getDialogs(DialogQueryDto dialogQuery) {
        return new DialogsWrapperDto(dialogService.searchDialogs(dialogQuery));
    }

    @GetMapping("/{id}")
    public DialogWrapperDto getDialog(@PathVariable String id) {
        return new DialogWrapperDto(dialogService.getDialog(id));
    }

    @PostMapping("/{id}/operator")
    public DialogWrapperDto assignOperator(@PathVariable String id,
                                           @AuthenticationPrincipal UserDetailsDto userDetails) {
        return new DialogWrapperDto(dialogService.assignOperator(id, userDetails));
    }

    @PostMapping("/{id}/messages")
    public DialogWrapperDto addMessage(@PathVariable String id,
                                       @RequestBody MessageRequestDto messageRequest,
                                       @AuthenticationPrincipal  UserDetailsDto userDetails) {
        return new DialogWrapperDto(dialogService.addMessage(id, messageRequest, userDetails));
    }

    @PostMapping("/{id}/close")
    public DialogWrapperDto closeDialog(@PathVariable String id,
                                        @AuthenticationPrincipal  UserDetailsDto userDetails) {
        return new DialogWrapperDto(dialogService.closeDialog(id, userDetails));
    }
}
