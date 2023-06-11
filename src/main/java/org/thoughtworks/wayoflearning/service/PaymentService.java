package org.thoughtworks.wayoflearning.service;


import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thoughtworks.wayoflearning.enums.PaymentStatus;
import org.thoughtworks.wayoflearning.infrastructure.client.PaymentClient;
import org.thoughtworks.wayoflearning.infrastructure.repository.PaymentRepository;
import org.thoughtworks.wayoflearning.mapper.PaymentMapper;
import org.thoughtworks.wayoflearning.model.request.PaymentRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;
    private final PaymentMapper paymentMapper;

    public String pay(PaymentRequest paymentRequest) {

        var paymentEntity = paymentMapper.toEntity(paymentRequest);
        try {
            var payResult = paymentClient.pay(paymentRequest);
            if (payResult.getBody().isSuccess()) {
                paymentEntity.setStatus(PaymentStatus.SUCCESS);
            }
        } catch (FeignClientException feignException) {
            if (HttpStatus.valueOf(feignException.status()).is4xxClientError()) {
                paymentEntity.setStatus(PaymentStatus.INSUFFICIENT_BALANCE);
            } else {
                paymentEntity.setStatus(PaymentStatus.FAILED);
            }

        }
        paymentRepository.save(paymentEntity);
        return paymentRepository.findByContractIdAndPayId(paymentRequest.getContractId(), paymentRequest.getPayId())
                .getStatus()
                .getMessage();
    }

}
