package org.thoughtworks.wayoflearning.service;


import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.thoughtworks.wayoflearning.enums.DataSynchronizationStatus;
import org.thoughtworks.wayoflearning.infrastructure.client.LearningManagementSystemClient;
import org.thoughtworks.wayoflearning.infrastructure.repository.DataSynchronizationRepository;
import org.thoughtworks.wayoflearning.mapper.DataSynchronizationMapper;
import org.thoughtworks.wayoflearning.model.message.DataSynchronizationMessage;
import org.thoughtworks.wayoflearning.model.request.LearningManagementRequest;

import static org.thoughtworks.wayoflearning.enums.Constant.TOPIC_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSynchronizationService {

    private final LearningManagementSystemClient learningManagementSystemClient;
    private final KafkaTemplate<Long, DataSynchronizationMessage> kafkaTemplate;
    private final DataSynchronizationRepository dataSynchronizationRepository;
    private final DataSynchronizationMapper dataSynchronizationMapper;

    public String sendData(LearningManagementRequest request) {
        var entity = dataSynchronizationMapper.toEntity(request);
        try {
            var payResult = learningManagementSystemClient.sendData(request);
            if (payResult.getBody().isSuccess()) {
                entity.setStatus(DataSynchronizationStatus.COMPLETE);
            }
        } catch (FeignClientException feignException) {
            if (HttpStatus.valueOf(feignException.status()).is4xxClientError()) {
                entity.setStatus(DataSynchronizationStatus.REQUIRED_INFO_MISSING);
            } else {
                entity.setStatus(DataSynchronizationStatus.IN_PROCESS);
                kafkaTemplate.send(TOPIC_NAME, request.getDataId(), dataSynchronizationMapper.toMessage(request));
            }

        }
        dataSynchronizationRepository.save(entity);
        return dataSynchronizationRepository.findByContractIdAndDataId(request.getContractId(), request.getDataId())
                .getStatus()
                .getMessage();
    }

}
