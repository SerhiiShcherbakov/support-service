package com.serhiishcherbakov.support.domain.dialog;

import com.serhiishcherbakov.support.api.request.CloseDialogRequestDto;
import com.serhiishcherbakov.support.api.request.MessageRequestDto;
import com.serhiishcherbakov.support.domain.dialog.entity.Dialog;
import com.serhiishcherbakov.support.domain.dialog.entity.DialogSummary;
import com.serhiishcherbakov.support.domain.user.UserService;
import com.serhiishcherbakov.support.exception.DialogNotFoundException;
import com.serhiishcherbakov.support.security.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDialogService {
    private final DialogRepositoryAdapter dialogRepository;
    private final DialogValidator dialogValidator;
    private final UserService userService;

    public List<DialogSummary> getDialogs(UserDetails userDetails) {
        var sortByUpdatedAt = Sort.by(Sort.Direction.DESC, "updatedAt");
        return dialogRepository.findAllByOwnerIdAndDeletedAtIsNull(userDetails.id(), sortByUpdatedAt);
    }

    public Dialog getDialog(String id, UserDetails userDetails) {
        var user = userService.syncAndGetUser(userDetails);
        return dialogRepository.findByIdAndDeletedAtIsNull(id)
                .map(dialog -> {
                    dialog.validateOwnership(user);
                    return dialog;
                })
                .orElseThrow(DialogNotFoundException::new);
    }

    public Dialog createDialog(MessageRequestDto messageRequest, UserDetails userDetails) {
        dialogValidator.validateDialogMessage(messageRequest);

        var user = userService.syncAndGetUser(userDetails);
        return dialogRepository.save(Dialog.newDialog(messageRequest, user));
    }

    public Dialog addUserMessage(String id, MessageRequestDto messageRequest, UserDetails userDetails) {
        dialogValidator.validateDialogMessage(messageRequest);

        var user = userService.syncAndGetUser(userDetails);
        var dialog = dialogRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(DialogNotFoundException::new);
        dialog.addUserMessage(messageRequest, user);
        return dialogRepository.save(dialog);
    }

    public Dialog closeDialog(String id, CloseDialogRequestDto closeDialogRequest, UserDetails userDetails) {
        dialogValidator.validateCloseDialogRequest(closeDialogRequest);

        var user = userService.syncAndGetUser(userDetails);
        var dialog = dialogRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(DialogNotFoundException::new);
        dialog.closeByUser(closeDialogRequest, user);
        return dialogRepository.save(dialog);
    }

    public void deleteDialog(String id, UserDetails userDetails) {
        var user = userService.syncAndGetUser(userDetails);
        var dialog = dialogRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(DialogNotFoundException::new);
        dialog.deleteByUser(user);
        dialogRepository.save(dialog);
    }
}
