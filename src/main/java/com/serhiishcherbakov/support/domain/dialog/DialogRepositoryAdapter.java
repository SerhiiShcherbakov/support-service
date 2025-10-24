package com.serhiishcherbakov.support.domain.dialog;

import com.serhiishcherbakov.support.api.request.DialogQueryDto;
import com.serhiishcherbakov.support.domain.dialog.entity.Dialog;
import com.serhiishcherbakov.support.domain.dialog.entity.DialogSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DialogRepositoryAdapter {
    private final DialogRepository dialogRepository;
    private final MongoTemplate mongoTemplate;

    public List<DialogSummary> findAll(DialogQueryDto dialogQuery, Sort sort) {
        List<AggregationOperation> pipeline = new ArrayList<>();

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
            pipeline.add(Aggregation.match(criteria));
        }

        pipeline.add(Aggregation.sort(sort));
        pipeline.add(Aggregation.lookup("users", "ownerId", "_id", "owner"));
        pipeline.add(Aggregation.lookup("users", "operatorId", "_id", "operator"));
        pipeline.add(Aggregation.unwind("owner", true));
        pipeline.add(Aggregation.unwind("operator", true));
        pipeline.add(Aggregation.project("id", "status", "createdAt", "updatedAt", "deletedAt", "owner", "operator", "version")
                .and(ArrayOperators.ArrayElemAt.arrayOf("messages").elementAt(-1))
        );

        return mongoTemplate.aggregate(Aggregation.newAggregation(pipeline), "dialogs", DialogSummary.class).getMappedResults();
    }

    public List<DialogSummary> findAllByOwnerIdAndDeletedAtIsNull(String userId) {
        return dialogRepository.findAllByOwnerIdAndDeletedAtIsNull(userId);
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
