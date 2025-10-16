package com.serhiishcherbakov.support.api;

import com.serhiishcherbakov.support.api.request.DialogQueryDto;
import com.serhiishcherbakov.support.api.request.MessageRequestDto;
import com.serhiishcherbakov.support.api.response.DialogWrapperDto;
import com.serhiishcherbakov.support.api.response.DialogsWrapperDto;
import com.serhiishcherbakov.support.domain.dialog.DialogService;
import com.serhiishcherbakov.support.security.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.serhiishcherbakov.support.security.UserInterceptor.USER_DETAILS_ATTRIBUTE;

@RestController
@RequestMapping("/api/operator/dialogs")
@RequiredArgsConstructor
public class OperatorDialogController {
    private final DialogService dialogService;

    public DialogsWrapperDto getDialogs(DialogQueryDto dialogQuery,
                                        @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new DialogsWrapperDto(dialogService.getOperatorDialogs(dialogQuery, userDetails));
    }

    @GetMapping("/{id}")
    public DialogWrapperDto getDialog(@PathVariable String id,
                                      @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new DialogWrapperDto(dialogService.getOperatorDialog(id, userDetails));
    }

    @PostMapping("/{id}/operator")
    public DialogWrapperDto assignOperator(@PathVariable String id,
                                           @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new DialogWrapperDto(dialogService.assignOperator(id, userDetails));
    }

    @PostMapping("/{id}/messages")
    public DialogWrapperDto addMessage(@PathVariable String id,
                                       @RequestBody MessageRequestDto messageRequest,
                                       @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new DialogWrapperDto(dialogService.addOperatorMessage(id, messageRequest, userDetails));
    }

    @PostMapping("/{id}/close")
    public DialogWrapperDto closeDialog(@PathVariable String id,
                                        @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new DialogWrapperDto(dialogService.closeDialogByOperator(id, userDetails));
    }
}
