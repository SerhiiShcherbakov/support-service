package com.serhiishcherbakov.support.domain.dialog;

import com.serhiishcherbakov.support.api.request.DialogQueryDto;
import com.serhiishcherbakov.support.domain.dialog.entity.Dialog;
import com.serhiishcherbakov.support.domain.dialog.entity.DialogSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DialogRepositoryAdapter {
    private final DialogRepository dialogRepository;
    private final MongoTemplate mongoTemplate;

    public List<DialogSummary> findAll(DialogQueryDto dialogQuery, Sort sort) {
        var query = new Query();

        var criteria = new Criteria();
        if (dialogQuery.status() != null) {
            criteria = criteria.and("status").is(dialogQuery.status());
        }
        if (dialogQuery.ownerId() != null) {
            criteria = criteria.and("ownerId").is(dialogQuery.ownerId());
        }
        if (dialogQuery.operatorId() != null) {
            criteria = criteria.and("operatorId").is(dialogQuery.operatorId());
        }
        if (!criteria.getCriteriaObject().isEmpty()) {
            query.addCriteria(criteria);
        }

        query.with(sort);

        query.fields().include("id", "status", "createdAt", "updatedAt", "deletedAt", "ownerId", "operatorId", "version");

        return mongoTemplate.find(query, DialogSummary.class, "dialogs");
    }

    public List<DialogSummary> findAllByOwnerIdAndDeletedAtIsNull(String userId, Sort sort) {
        return dialogRepository.findAllByOwnerIdAndDeletedAtIsNull(userId, sort);
    }

    public Optional<Dialog> findByIdAndDeletedAtIsNull(String id) {
        return dialogRepository.findByIdAndDeletedAtIsNull(id);
    }

    public Optional<Dialog> findById(String id) {
        return dialogRepository.findById(id);
    }

    public Dialog save(Dialog dialog) {
        return dialogRepository.save(dialog);
    }
}
