package org.thoughtworks.wayoflearning.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSynchronizationMessage {

    private Long contractId;

    private Long dataId;

    private String studentAccount;

    private String subject;

    private String subjectName;

    private int duration;

    private String unit;

}
