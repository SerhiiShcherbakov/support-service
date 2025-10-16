package com.serhiishcherbakov.support.api;

import com.serhiishcherbakov.support.api.request.CloseDialogRequestDto;
import com.serhiishcherbakov.support.api.request.MessageRequestDto;
import com.serhiishcherbakov.support.api.response.UserDialogDto;
import com.serhiishcherbakov.support.api.response.UserDialogsDto;
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
    public UserDialogsDto getUserDialogs(@RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new UserDialogsDto(dialogService.getUserDialogs(userDetails));
    }

    @GetMapping("/{id}")
    public UserDialogDto getUserDialog(@PathVariable String id,
                                       @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new UserDialogDto(dialogService.getUserDialog(id, userDetails));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDialogDto createDialog(@RequestBody MessageRequestDto messageRequest,
                                      @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new UserDialogDto(dialogService.createDialog(messageRequest, userDetails));
    }

    @PostMapping("/{id}/messages")
    public UserDialogDto addMessage(@PathVariable String id,
                                    @RequestBody MessageRequestDto messageRequest,
                                    @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new UserDialogDto(dialogService.addUserMessage(id, messageRequest, userDetails));
    }

    @PostMapping("/{id}/close")
    public UserDialogDto closeDialog(@PathVariable String id,
                                     @RequestBody CloseDialogRequestDto closeDialogRequest,
                                     @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        return new UserDialogDto(dialogService.closeDialogByUser(id, closeDialogRequest, userDetails));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteDialog(@PathVariable String id,
                             @RequestAttribute(USER_DETAILS_ATTRIBUTE) UserDetails userDetails) {
        dialogService.deleteDialog(id, userDetails);
    }
}
