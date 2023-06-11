package org.thoughtworks.wayoflearning.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String code;

    private String message;


    @JsonIgnore
    public boolean isSuccess(){
        return StringUtils.equalsIgnoreCase("SUCCESS", code);
    }


    public static PaymentResponse success(){
        return PaymentResponse.builder().code("SUCCESS").message("支付成功").build();
    }

}
