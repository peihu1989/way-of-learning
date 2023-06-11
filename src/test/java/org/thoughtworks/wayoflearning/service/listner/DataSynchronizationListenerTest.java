package org.thoughtworks.wayoflearning.service.listner;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.thoughtworks.wayoflearning.WayOfLearningApplicationBaseTests;
import org.thoughtworks.wayoflearning.model.message.DataSynchronizationMessage;
import org.thoughtworks.wayoflearning.model.request.LearningManagementRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.thoughtworks.wayoflearning.enums.Constant.TOPIC_NAME;

class DataSynchronizationListenerTest extends WayOfLearningApplicationBaseTests {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Captor
    private ArgumentCaptor<LearningManagementRequest> messageCaptor;

    @SneakyThrows
    @Test
    void should_resend_to_learning_management_system_when_listen_to_message_from_topic() {
        // given
        var message = DataSynchronizationMessage.builder()
                .contractId(1L)
                .dataId(1L)
                .studentAccount("Lucy")
                .subject("Math")
                .subjectName("李永乐老师讲XXX")
                .duration(30)
                .unit("min").build();

        // when
        kafkaTemplate.send(TOPIC_NAME, message.getDataId(), message).get();

        //then
        verify(dataSynchronizationService, timeout(5000))
                .sendData(messageCaptor.capture());
        assertThat(messageCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(message);
    }
}