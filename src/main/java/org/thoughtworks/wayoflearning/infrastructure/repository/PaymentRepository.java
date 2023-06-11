package org.thoughtworks.wayoflearning.infrastructure.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thoughtworks.wayoflearning.model.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {

    PaymentEntity findByContractIdAndPayId(long contractId,long payId);

}
