package org.thoughtworks.wayoflearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.thoughtworks.wayoflearning.model.dto.DataSynchronizationConfirmation;
import org.thoughtworks.wayoflearning.model.entity.DataSynchronizationEntity;
import org.thoughtworks.wayoflearning.model.entity.PaymentEntity;
import org.thoughtworks.wayoflearning.model.message.DataSynchronizationMessage;
import org.thoughtworks.wayoflearning.model.request.LearningManagementRequest;
import org.thoughtworks.wayoflearning.model.request.PaymentRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataSynchronizationMapper {

    LearningManagementRequest toRequest(long contractId, long dataId, DataSynchronizationConfirmation confirmation);

    DataSynchronizationEntity toEntity(LearningManagementRequest request);

    DataSynchronizationMessage toMessage(LearningManagementRequest request);

    LearningManagementRequest toRequest(DataSynchronizationMessage message);

}
