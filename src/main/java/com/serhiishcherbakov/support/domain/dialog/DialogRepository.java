package com.serhiishcherbakov.support.domain.dialog;

import com.serhiishcherbakov.support.domain.dialog.entity.Dialog;
import com.serhiishcherbakov.support.domain.dialog.entity.projection.DialogSummary;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DialogRepository extends MongoRepository<Dialog, String> {
    @Aggregation(pipeline = {
            "{ $match: { ownerId: ?0, deletedAt: null } }",
            "{ $sort: { updatedAt: -1 } }",
            "{ $lookup: { from: 'users', localField: 'ownerId', foreignField: '_id', as: 'owner' } }",
            "{ $lookup: { from: 'users', localField: 'operatorId', foreignField: '_id', as: 'operator' } }",
            "{ $unwind: { path: '$owner', preserveNullAndEmptyArrays: true } }",
            "{ $unwind: { path: '$operator', preserveNullAndEmptyArrays: true } }",
            """
                    {
                        $project: {
                            id: 1,
                            status: 1,
                            createdAt: 1,
                            updatedAt: 1,
                            deletedAt: 1,
                            owner: 1,
                            operator: 1,
                            version: 1,
                            lastMessage: { $arrayElemAt: ["$messages", -1] }
                        }
                    }
            """
    })
    List<DialogSummary> findAllByOwnerIdAndDeletedAtIsNull(String userId);

    Optional<Dialog> findByIdAndDeletedAtIsNull(String id);
}
