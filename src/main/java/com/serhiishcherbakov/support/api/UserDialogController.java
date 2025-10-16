package com.serhiishcherbakov.support.api;

import com.serhiishcherbakov.support.api.request.CloseDialogRequestDto;
import com.serhiishcherbakov.support.api.request.MessageRequestDto;
import com.serhiishcherbakov.support.api.response.DialogWrapperDto;
import com.serhiishcherbakov.support.api.response.DialogsWrapperDto;
import com.serhiishcherbakov.support.domain.dialog.DialogService;
import com.serhiishcherbakov.support.security.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.serhiishcherbakov.support.security.UserInterceptor.USER_DETAILS_ATTRIBUTE;

@RestController
@RequestMapping("/api/user/dialogs")
@RequiredArgsConstructor
public class UserDialogController {
    private final DialogService dialogService;

    @GetMapping
    public DialogsWrapperDto getUserDialogs(@RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new DialogsWrapperDto(dialogService.getUserDialogs(userDetails));
    }

    @GetMapping("/{id}")
    public DialogWrapperDto getUserDialog(@PathVariable String id,
                                          @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new DialogWrapperDto(dialogService.getUserDialog(id, userDetails));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public DialogWrapperDto createDialog(@RequestBody MessageRequestDto messageRequest,
                                         @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new DialogWrapperDto(dialogService.createDialog(messageRequest, userDetails));
    }

    @PostMapping("/{id}/messages")
    public DialogWrapperDto addMessage(@PathVariable String id,
                                       @RequestBody MessageRequestDto messageRequest,
                                       @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new DialogWrapperDto(dialogService.addUserMessage(id, messageRequest, userDetails));
    }

    @PostMapping("/{id}/close")
7    public DialogWrapperDto closeDialog(@PathVariable String id,
                                        @RequestBody CloseDialogRequestDto closeDialogRequest,
                                        @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new DialogWrapperDto(dialogService.closeDialogByUser(id, closeDialogRequest, userDetails));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteDialog(@PathVariable String id,
                             @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        dialogService.deleteDialog(id, userDetails);
    }
}
