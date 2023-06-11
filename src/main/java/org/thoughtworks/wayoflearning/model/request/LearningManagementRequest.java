package org.thoughtworks.wayoflearning.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningManagementRequest {

    private Long contractId;

    private Long dataId;

    private String studentAccount;

    private String subject;

    private String subjectName;

    private int duration;

    private String unit;

}
