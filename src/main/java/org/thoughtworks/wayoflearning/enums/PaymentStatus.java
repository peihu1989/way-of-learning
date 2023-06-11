package org.thoughtworks.wayoflearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    SUCCESS("支付成功"),
    INSUFFICIENT_BALANCE("支付失败,余额不足"),
    FAILED("支付失败");

    private final String message;
}
