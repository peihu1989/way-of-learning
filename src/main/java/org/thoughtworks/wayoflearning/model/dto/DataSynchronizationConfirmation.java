package org.thoughtworks.wayoflearning.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSynchronizationConfirmation {

    private String studentAccount;

    private String subject;

    private String subjectName;

    private int duration;

    private String unit;

}
