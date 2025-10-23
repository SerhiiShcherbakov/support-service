package com.serhiishcherbakov.support.domain.dialog;

import com.serhiishcherbakov.support.api.request.CloseDialogRequestDto;
import com.serhiishcherbakov.support.api.request.MessageRequestDto;
import com.serhiishcherbakov.support.domain.dialog.entity.Dialog;
import com.serhiishcherbakov.support.domain.dialog.entity.DialogSummary;
import com.serhiishcherbakov.support.exception.DialogNotFoundException;
import com.serhiishcherbakov.support.security.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDialogService {
    private final DialogRepositoryAdapter dialogRepository;
    private final DialogValidator dialogValidator;

    public List<DialogSummary> getDialogs(UserDetailsDto userDetails) {
        return dialogRepository.findAllByOwnerIdAndDeletedAtIsNull(userDetails.id());
    }

    public Dialog getDialog(String id, UserDetailsDto userDetails) {
        return dialogRepository.findByIdAndDeletedAtIsNull(id)
                .map(dialog -> {
                    dialog.validateOwnership(userDetails);
                    return dialog;
                })
                .orElseThrow(DialogNotFoundException::new);
    }

    public Dialog createDialog(MessageRequestDto messageRequest, UserDetailsDto userDetails) {
        dialogValidator.validateDialogMessage(messageRequest);

        return dialogRepository.save(Dialog.newDialog(messageRequest, userDetails));
    }

    public Dialog addUserMessage(String id, MessageRequestDto messageRequest, UserDetailsDto userDetails) {
        dialogValidator.validateDialogMessage(messageRequest);

        var dialog = dialogRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(DialogNotFoundException::new);
        dialog.addUserMessage(messageRequest, userDetails);
        return dialogRepository.save(dialog);
    }

    public Dialog closeDialog(String id, CloseDialogRequestDto closeDialogRequest, UserDetailsDto userDetails) {
        dialogValidator.validateCloseDialogRequest(closeDialogRequest);

        var dialog = dialogRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(DialogNotFoundException::new);
        dialog.closeByUser(closeDialogRequest, userDetails);
        return dialogRepository.save(dialog);
    }

    public void deleteDialog(String id, UserDetailsDto userDetails) {
        var dialog = dialogRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(DialogNotFoundException::new);
        dialog.deleteByUser(userDetails);
        dialogRepository.save(dialog);
    }
}
