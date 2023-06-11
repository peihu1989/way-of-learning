package org.thoughtworks.wayoflearning.infrastructure.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.thoughtworks.wayoflearning.enums.PaymentStatus;
import org.thoughtworks.wayoflearning.model.entity.PaymentEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
    }

    @Test
    void should_get_correct_payment_entity_data_when_retrieve_from_database() {
        //given
        PaymentEntity original = PaymentEntity.builder()
                .contractId(1L)
                .payId(2L)
                .amount(BigDecimal.ONE)
                .status(PaymentStatus.SUCCESS)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        paymentRepository.save(original);

        // when
        PaymentEntity result = paymentRepository.findByContractIdAndPayId(original.getContractId(), original.getPayId());

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(original);
    }
}