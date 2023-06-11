package org.thoughtworks.wayoflearning;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thoughtworks.wayoflearning.service.DataSynchronizationService;
import org.thoughtworks.wayoflearning.service.PaymentService;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(value = {SpringExtension.class})
@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class WayOfLearningApplicationBaseTests {

    @MockBean
    protected PaymentService paymentService;

    @MockBean
    protected DataSynchronizationService dataSynchronizationService;

}
