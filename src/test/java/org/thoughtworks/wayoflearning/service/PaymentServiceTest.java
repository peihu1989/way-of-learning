package org.thoughtworks.wayoflearning.service;

import feign.FeignException.FeignClientException;
import feign.Request;
import feign.Request.Body;
import feign.Request.HttpMethod;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.thoughtworks.wayoflearning.enums.PaymentStatus;
import org.thoughtworks.wayoflearning.infrastructure.client.PaymentClient;
import org.thoughtworks.wayoflearning.infrastructure.repository.PaymentRepository;
import org.thoughtworks.wayoflearning.mapper.PaymentMapper;
import org.thoughtworks.wayoflearning.model.entity.PaymentEntity;
import org.thoughtworks.wayoflearning.model.request.PaymentRequest;
import org.thoughtworks.wayoflearning.model.response.PaymentResponse;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private static PaymentService paymentService;

    private static PaymentRepository paymentRepository;
    private static PaymentClient paymentClient;
    private static PaymentMapper paymentMapper;

    @BeforeAll
    static void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentClient = mock(PaymentClient.class);
        paymentMapper = mock(PaymentMapper.class);
        paymentService = new PaymentService(paymentRepository, paymentClient, paymentMapper);
    }

    @Test
    void should_return_success_when_payment_client_return_pay_success() {
        // given
        var paymentRequest = PaymentRequest.builder().contractId(1L).payId(1L).amount(BigDecimal.ONE).build();
        var paymentEntity = PaymentEntity.builder().build();
        when(paymentMapper.toEntity(paymentRequest))
                .thenReturn(paymentEntity);
        when(paymentClient.pay(paymentRequest)).thenReturn(ResponseEntity.ok(PaymentResponse.success()));
        when(paymentRepository.findByContractIdAndPayId(paymentRequest.getContractId(), paymentRequest.getPayId()))
                .thenReturn(PaymentEntity.builder()
                        .contractId(paymentRequest.getContractId())
                        .payId(paymentRequest.getPayId())
                        .status(PaymentStatus.SUCCESS)
                        .build()
        );

        // when
        var payResult = paymentService.pay(paymentRequest);

        // then
        verify(paymentClient, times(1)).pay(paymentRequest);
        verify(paymentRepository, times(1)).save(paymentEntity);
        verify(paymentRepository, times(1)).findByContractIdAndPayId(paymentRequest.getContractId(), paymentRequest.getPayId());
        assertEquals(PaymentStatus.SUCCESS.getMessage(), payResult);
    }

    @Test
    void should_return_insufficient_balance_when_feign_client_return_400() {
        // given
        var paymentRequest = PaymentRequest.builder().contractId(2L).payId(2L).amount(BigDecimal.ONE).build();
        var paymentEntity = PaymentEntity.builder().build();
        when(paymentMapper.toEntity(paymentRequest))
                .thenReturn(paymentEntity);
        var request = Request.create(HttpMethod.POST, "fakeUrl", Map.of(), Body.create(""), null);
        when(paymentClient.pay(paymentRequest)).thenThrow(
                new FeignClientException(400, "Bad Request", request, null, null));
        when(paymentRepository.findByContractIdAndPayId(paymentRequest.getContractId(), paymentRequest.getPayId()))
                .thenReturn(PaymentEntity.builder()
                        .contractId(paymentRequest.getContractId())
                        .payId(paymentRequest.getPayId())
                        .status(PaymentStatus.INSUFFICIENT_BALANCE)
                        .build()
        );

        // when
        var payResult = paymentService.pay(paymentRequest);

        // then
        verify(paymentClient, times(1)).pay(paymentRequest);
        verify(paymentRepository, times(1)).save(paymentEntity);
        verify(paymentRepository, times(1)).findByContractIdAndPayId(paymentRequest.getContractId(), paymentRequest.getPayId());
        assertEquals(PaymentStatus.INSUFFICIENT_BALANCE.getMessage(), payResult);
    }

    @Test
    void should_return_failed_when_feign_client_return_500() {
        // given
        var paymentRequest = PaymentRequest.builder().contractId(3L).payId(3L).amount(BigDecimal.ONE).build();
        var paymentEntity = PaymentEntity.builder().build();
        when(paymentMapper.toEntity(paymentRequest)).thenReturn(paymentEntity);
        var request = Request.create(HttpMethod.POST, "fakeUrl", Map.of(), Body.create(""), null);
        when(paymentClient.pay(paymentRequest)).thenThrow(
                new FeignClientException(500, "Server Error", request, null, null));
        when(paymentRepository.findByContractIdAndPayId(paymentRequest.getContractId(), paymentRequest.getPayId()))
                .thenReturn(PaymentEntity.builder()
                        .contractId(paymentRequest.getContractId())
                        .payId(paymentRequest.getPayId())
                        .status(PaymentStatus.FAILED)
                        .build()
                );

        // when
        var payResult = paymentService.pay(paymentRequest);

        // then
        verify(paymentClient, times(1)).pay(paymentRequest);
        verify(paymentRepository, times(1)).save(paymentEntity);
        verify(paymentRepository, times(1)).findByContractIdAndPayId(paymentRequest.getContractId(), paymentRequest.getPayId());
        assertEquals(PaymentStatus.FAILED.getMessage(), payResult);
    }
}