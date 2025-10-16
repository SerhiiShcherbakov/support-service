package com.serhiishcherbakov.support.domain.dialog;

import com.serhiishcherbakov.support.domain.dialog.entity.Dialog;
import com.serhiishcherbakov.support.domain.dialog.entity.DialogSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DialogRepository extends MongoRepository<Dialog, String> {
    List<DialogSummary> findAllByOwnerId(String userId);
}
