package com.serhiishcherbakov.support.domain.dialog;

import com.serhiishcherbakov.support.api.request.DialogQueryDto;
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
public class OperatorDialogService {
    private final DialogRepositoryAdapter dialogRepository;
    private final DialogValidator dialogValidator;

    public List<DialogSummary> searchDialogs(DialogQueryDto dialogQuery) {
        var sortByUpdatedAt = Sort.by(Sort.Direction.DESC, "updatedAt");
        return dialogRepository.findAll(dialogQuery, sortByUpdatedAt);
    }

    public Dialog getDialog(String id) {
        return dialogRepository.findById(id).orElseThrow(DialogNotFoundException::new);
    }

    public Dialog assignOperator(String id, UserDetailsDto userDetails) {
        var dialog = getDialog(id);
        dialog.assignOperator(userDetails);
        return dialogRepository.save(dialog);
    }

    public Dialog addMessage(String id, MessageRequestDto messageRequest, UserDetailsDto userDetails) {
        dialogValidator.validateDialogMessage(messageRequest);
        var dialog = getDialog(id);
        dialog.addOperatorMessage(messageRequest, userDetails);
        return dialogRepository.save(dialog);
    }

    public Dialog closeDialog(String id, UserDetailsDto userDetails) {
        var dialog = getDialog(id);
        dialog.closeByOperator(userDetails);
        return dialogRepository.save(dialog);
    }
}
