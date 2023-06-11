package org.thoughtworks.wayoflearning.service.listner;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.thoughtworks.wayoflearning.mapper.DataSynchronizationMapper;
import org.thoughtworks.wayoflearning.model.message.DataSynchronizationMessage;
import org.thoughtworks.wayoflearning.model.request.LearningManagementRequest;
import org.thoughtworks.wayoflearning.service.DataSynchronizationService;

import static org.thoughtworks.wayoflearning.enums.Constant.TOPIC_NAME;

@Service
@RequiredArgsConstructor
public class DataSynchronizationListener {

    private final DataSynchronizationService dataSynchronizationService;

    private final DataSynchronizationMapper dataSynchronizationMapper;

    @KafkaListener(topics = TOPIC_NAME,
            id = "school-service.data-synchronization-request.listener",
            properties = {
                    "spring.json.use.type.headers=false",
                    "spring.json.value.default.type=org.thoughtworks.wayoflearning.model.message.DataSynchronizationMessage"
            })
    public void listenOrderModelingTopic(DataSynchronizationMessage message) {
        dataSynchronizationService.sendData(dataSynchronizationMapper.toRequest(message));
    }

}
