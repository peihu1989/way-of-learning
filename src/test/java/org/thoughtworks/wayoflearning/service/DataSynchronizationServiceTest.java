package org.thoughtworks.wayoflearning.service;

import feign.FeignException.FeignClientException;
import feign.Request;
import feign.Request.Body;
import feign.Request.HttpMethod;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.thoughtworks.wayoflearning.enums.DataSynchronizationStatus;
import org.thoughtworks.wayoflearning.enums.PaymentStatus;
import org.thoughtworks.wayoflearning.infrastructure.client.LearningManagementSystemClient;
import org.thoughtworks.wayoflearning.infrastructure.client.PaymentClient;
import org.thoughtworks.wayoflearning.infrastructure.repository.DataSynchronizationRepository;
import org.thoughtworks.wayoflearning.infrastructure.repository.PaymentRepository;
import org.thoughtworks.wayoflearning.mapper.DataSynchronizationMapper;
import org.thoughtworks.wayoflearning.mapper.PaymentMapper;
import org.thoughtworks.wayoflearning.model.entity.DataSynchronizationEntity;
import org.thoughtworks.wayoflearning.model.entity.PaymentEntity;
import org.thoughtworks.wayoflearning.model.message.DataSynchronizationMessage;
import org.thoughtworks.wayoflearning.model.request.LearningManagementRequest;
import org.thoughtworks.wayoflearning.model.request.PaymentRequest;
import org.thoughtworks.wayoflearning.model.response.LearningManagementResponse;
import org.thoughtworks.wayoflearning.model.response.PaymentResponse;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.thoughtworks.wayoflearning.enums.Constant.TOPIC_NAME;

@ExtendWith(MockitoExtension.class)
class DataSynchronizationServiceTest {

    private static DataSynchronizationService dataSynchronizationService;

    private static DataSynchronizationRepository dataSynchronizationRepository;
    private static LearningManagementSystemClient learningManagementSystemClient;
    private static DataSynchronizationMapper dataSynchronizationMapper;
    private static KafkaTemplate<Long, DataSynchronizationMessage> kafkaTemplate;

    @BeforeAll
    static void setUp() {
        dataSynchronizationRepository = mock(DataSynchronizationRepository.class);
        learningManagementSystemClient = mock(LearningManagementSystemClient.class);
        dataSynchronizationMapper = mock(DataSynchronizationMapper.class);
        kafkaTemplate = mock(KafkaTemplate.class);
        dataSynchronizationService = new DataSynchronizationService(learningManagementSystemClient,
                kafkaTemplate, dataSynchronizationRepository, dataSynchronizationMapper);
    }

    @Test
    void should_return_success_when_learning_management_client_return_synchronization_complete() {
        // given
        var request = LearningManagementRequest.builder().contractId(1L).dataId(1L).build();
        var entity = DataSynchronizationEntity.builder().build();
        when(dataSynchronizationMapper.toEntity(request))
                .thenReturn(entity);
        when(learningManagementSystemClient.sendData(request)).thenReturn(
                ResponseEntity.ok(LearningManagementResponse.success()));
        when(dataSynchronizationRepository.findByContractIdAndDataId(request.getContractId(), request.getDataId()))
                .thenReturn(DataSynchronizationEntity.builder()
                        .contractId(request.getContractId())
                        .dataId(request.getDataId())
                        .status(DataSynchronizationStatus.COMPLETE)
                        .build()
                );

        // when
        var result = dataSynchronizationService.sendData(request);

        // then
        verify(learningManagementSystemClient, times(1)).sendData(request);
        verify(dataSynchronizationRepository, times(1)).save(entity);
        verify(dataSynchronizationRepository, times(1))
                .findByContractIdAndDataId(request.getContractId(), request.getDataId());
        verify(kafkaTemplate, never()).send(any(), any(), any());
        assertEquals(DataSynchronizationStatus.COMPLETE.getMessage(), result);
    }

    @Test
    void should_return__when_learning_management_client_return_synchronization_failed() {
        // given
        var request = LearningManagementRequest.builder().contractId(1L).dataId(2L).build();
        var entity = DataSynchronizationEntity.builder().build();
        when(dataSynchronizationMapper.toEntity(request)).thenReturn(entity);
        var thirdSystemClientRequest = Request.create(HttpMethod.POST, "fakeUrl", Map.of(), Body.create(""), null);
        when(learningManagementSystemClient.sendData(request))
                .thenThrow(new FeignClientException(400, "Bad Request", thirdSystemClientRequest, null, null));
        when(dataSynchronizationRepository.findByContractIdAndDataId(request.getContractId(), request.getDataId()))
                .thenReturn(DataSynchronizationEntity.builder()
                        .contractId(request.getContractId())
                        .dataId(request.getDataId())
                        .status(DataSynchronizationStatus.REQUIRED_INFO_MISSING)
                        .build()
                );

        // when
        var result = dataSynchronizationService.sendData(request);

        // then
        verify(learningManagementSystemClient, times(1)).sendData(request);
        verify(dataSynchronizationRepository, times(1)).save(entity);
        verify(dataSynchronizationRepository, times(1))
                .findByContractIdAndDataId(request.getContractId(), request.getDataId());
        verify(kafkaTemplate, never()).send(any(), any(), any());
        assertEquals(DataSynchronizationStatus.REQUIRED_INFO_MISSING.getMessage(), result);
    }

    @Test
    void should_return_in_progress_when_feign_client_return_500() {
        // given
        var request = LearningManagementRequest.builder().contractId(1L).dataId(3L).build();
        var entity = DataSynchronizationEntity.builder().build();
        when(dataSynchronizationMapper.toEntity(request)).thenReturn(entity);
        var thirdSystemClientRequest = Request.create(HttpMethod.POST, "fakeUrl", Map.of(), Body.create(""), null);
        when(learningManagementSystemClient.sendData(request))
                .thenThrow(new FeignClientException(500, "Server Error", thirdSystemClientRequest, null, null));
        when(dataSynchronizationRepository.findByContractIdAndDataId(request.getContractId(), request.getDataId()))
                .thenReturn(DataSynchronizationEntity.builder()
                        .contractId(request.getContractId())
                        .dataId(request.getDataId())
                        .status(DataSynchronizationStatus.IN_PROCESS)
                        .build()
                );

        // when
        var result = dataSynchronizationService.sendData(request);

        // then
        verify(learningManagementSystemClient, times(1)).sendData(request);
        verify(dataSynchronizationRepository, times(1)).save(entity);
        verify(dataSynchronizationRepository, times(1))
                .findByContractIdAndDataId(request.getContractId(), request.getDataId());
        verify(kafkaTemplate, only()).send(any(), any(), any());
        assertEquals(DataSynchronizationStatus.IN_PROCESS.getMessage(), result);
    }
}