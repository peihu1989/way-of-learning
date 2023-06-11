package org.thoughtworks.wayoflearning.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.thoughtworks.wayoflearning.enums.DataSynchronizationStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "data_synchronization")
@EntityListeners(AuditingEntityListener.class)
public class DataSynchronizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long dataId;

    @Column
    private Long contractId;

    @Column
    private String studentAccount;

    @Column
    private String subject;

    @Column
    private String subjectName;

    @Column
    private int duration;

    @Column
    private String unit;

    @Column
    @Enumerated(value = EnumType.STRING)
    private DataSynchronizationStatus status;

    @Column
    @CreatedDate
    private LocalDateTime createAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updateAt;

}
