package org.thoughtworks.wayoflearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataSynchronizationStatus {

    COMPLETE("同步成功"),
    REQUIRED_INFO_MISSING("同步失败,缺少必要信息"),
    IN_PROCESS("同步中,请稍后查看同步结果");

    private final String message;
}
