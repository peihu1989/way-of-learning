package org.thoughtworks.wayoflearning.infrastructure.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.thoughtworks.wayoflearning.enums.DataSynchronizationStatus;
import org.thoughtworks.wayoflearning.enums.PaymentStatus;
import org.thoughtworks.wayoflearning.model.entity.DataSynchronizationEntity;
import org.thoughtworks.wayoflearning.model.entity.PaymentEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DataSynchronizationRepositoryTest {

    @Autowired
    private DataSynchronizationRepository dataSynchronizationRepository;

    @AfterEach
    void tearDown() {
        dataSynchronizationRepository.deleteAll();
    }

    @Test
    void should_get_correct_data_synchronization_entity_data_when_retrieve_from_database() {
        //given
        DataSynchronizationEntity original = DataSynchronizationEntity.builder()
                .contractId(1L)
                .dataId(2L)
                .studentAccount("Lucy")
                .subject("Math")
                .subjectName("李永乐老师讲XXX")
                .duration(30)
                .unit("min")
                .status(DataSynchronizationStatus.COMPLETE)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        dataSynchronizationRepository.save(original);

        // when
        DataSynchronizationEntity result = dataSynchronizationRepository.findByContractIdAndDataId(original.getContractId(), original.getDataId());

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(original);
    }
}