package com.serhiishcherbakov.support.domain.dialog;

import com.serhiishcherbakov.support.domain.dialog.entity.Dialog;
import com.serhiishcherbakov.support.domain.dialog.entity.DialogSummary;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DialogRepository extends MongoRepository<Dialog, String> {
    List<DialogSummary> findAllByOwnerIdAndDeletedAtIsNull(String userId, Sort sort);

    Optional<Dialog> findByIdAndDeletedAtIsNull(String id);
}
