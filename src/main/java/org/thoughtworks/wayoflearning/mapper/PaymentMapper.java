package org.thoughtworks.wayoflearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.thoughtworks.wayoflearning.model.dto.PaymentConfirmation;
import org.thoughtworks.wayoflearning.model.entity.PaymentEntity;
import org.thoughtworks.wayoflearning.model.request.PaymentRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    PaymentRequest toRequest(long contractId, long payId, PaymentConfirmation paymentConfirmation);

    PaymentEntity toEntity(PaymentRequest paymentRequest);

}
