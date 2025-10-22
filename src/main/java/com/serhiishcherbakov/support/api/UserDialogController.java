package com.serhiishcherbakov.support.api;

import com.serhiishcherbakov.support.api.request.CloseDialogRequestDto;
import com.serhiishcherbakov.support.api.request.MessageRequestDto;
import com.serhiishcherbakov.support.api.response.DialogWrapperDto;
import com.serhiishcherbakov.support.api.response.DialogsWrapperDto;
import com.serhiishcherbakov.support.domain.dialog.UserDialogService;
import com.serhiishcherbakov.support.security.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/dialogs")
@RequiredArgsConstructor
public class UserDialogController {
    private final UserDialogService userDialogService;

    @GetMapping
    public DialogsWrapperDto getUserDialogs(@AuthenticationPrincipal UserDetailsDto userDetails) {
        return new DialogsWrapperDto(userDialogService.getDialogs(userDetails));
    }

    @GetMapping("/{id}")
    public DialogWrapperDto getUserDialog(@PathVariable String id,
                                          @AuthenticationPrincipal  UserDetailsDto userDetails) {
        return new DialogWrapperDto(userDialogService.getDialog(id, userDetails));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public DialogWrapperDto createDialog(@RequestBody MessageRequestDto messageRequest,
                                         @AuthenticationPrincipal  UserDetailsDto userDetails) {
        return new DialogWrapperDto(userDialogService.createDialog(messageRequest, userDetails));
    }

    @PostMapping("/{id}/messages")
    public DialogWrapperDto addMessage(@PathVariable String id,
                                       @RequestBody MessageRequestDto messageRequest,
                                       @AuthenticationPrincipal  UserDetailsDto userDetails) {
        return new DialogWrapperDto(userDialogService.addUserMessage(id, messageRequest, userDetails));
    }

    @PostMapping("/{id}/close")
    public DialogWrapperDto closeDialog(@PathVariable String id,
                                        @RequestBody CloseDialogRequestDto closeDialogRequest,
                                        @AuthenticationPrincipal  UserDetailsDto userDetails) {
        return new DialogWrapperDto(userDialogService.closeDialog(id, closeDialogRequest, userDetails));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteDialog(@PathVariable String id,
                             @AuthenticationPrincipal  UserDetailsDto userDetails) {
        userDialogService.deleteDialog(id, userDetails);
    }
}
