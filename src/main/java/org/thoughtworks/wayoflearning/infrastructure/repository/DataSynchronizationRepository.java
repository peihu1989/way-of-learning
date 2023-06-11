package org.thoughtworks.wayoflearning.infrastructure.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thoughtworks.wayoflearning.model.entity.DataSynchronizationEntity;

@Repository
public interface DataSynchronizationRepository extends JpaRepository<DataSynchronizationEntity, Long> {

    DataSynchronizationEntity findByContractIdAndDataId(long contractId, long dataId);

}
